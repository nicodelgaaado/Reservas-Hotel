package com.reservas.reservas;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Prueba los contratos HTTP con los servicios y repositorios reales en un contexto aislado. */
class ReservasApiTest {
    @TempDir
    Path directorio;
    private ConfigurableApplicationContext contexto;
    private MockMvc mvc;
    private Path archivo;

    @BeforeEach
    void iniciar() {
        archivo = directorio.resolve("confirmaciones.txt");
        contexto = new SpringApplicationBuilder(Reservas.class)
                .web(WebApplicationType.SERVLET)
                .run("--server.port=0", "--reservas.demo.enabled=false",
                        "--reservas.archivo=" + archivo, "--logging.level.root=WARN");
        mvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) contexto).build();
    }

    @AfterEach
    void cerrar() {
        if (contexto != null) contexto.close();
    }

    @Test
    void registraCreaConsultaConfirmaYCancela() throws Exception {
        registrarDatos();
        crearReserva();
        mvc.perform(get("/api/reservas/1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.clienteId").value(1))
                .andExpect(jsonPath("$.numeroHabitacion").value(101))
                .andExpect(jsonPath("$.cliente").doesNotExist());
        mvc.perform(post("/api/reservas/1/confirmacion").contentType(MediaType.APPLICATION_JSON)
                .content("{\"tarifaBase\":100000}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("CONFIRMADA"));
        assertEquals(1, Files.readAllLines(archivo).size());
        assertTrue(Files.readString(archivo).contains("1|Ana|habitacion=101|"));
        mvc.perform(get("/api/habitaciones/101"))
                .andExpect(jsonPath("$.estado").value("OCUPADA"));
        mvc.perform(post("/api/reservas/1/cancelacion"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("CANCELADA"));
        mvc.perform(get("/api/habitaciones/101"))
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"));
        mvc.perform(post("/api/reservas/1/cancelacion")).andExpect(status().isConflict());
        assertEquals(1, Files.readAllLines(archivo).size());
    }

    @Test
    void evitaDobleConfirmacionYOcupacionYCancelacionPendiente() throws Exception {
        registrarDatos();
        crearReserva();
        mvc.perform(post("/api/reservas").contentType(MediaType.APPLICATION_JSON).content(reservaJson()))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/reservas/1/confirmacion").contentType(MediaType.APPLICATION_JSON)
                .content("{\"tarifaBase\":100000}"))
                .andExpect(status().isOk());
        for (int id : new int[] {1, 2}) {
            mvc.perform(post("/api/reservas/" + id + "/confirmacion")
                    .contentType(MediaType.APPLICATION_JSON).content("{\"tarifaBase\":100000}"))
                    .andExpect(status().isConflict());
        }
        mvc.perform(post("/api/reservas/2/cancelacion")).andExpect(status().isConflict());
        mvc.perform(get("/api/habitaciones/101"))
                .andExpect(jsonPath("$.estado").value("OCUPADA"));
        assertEquals(1, Files.readAllLines(archivo).size());
    }

    @Test
    void validaEntradasYNoCreaDatosInvalidos() throws Exception {
        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"nombre\":\"\",\"email\":\"invalido\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/reservas").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest());
        registrarDatos();
        var fecha = LocalDate.now().plusDays(10);
        mvc.perform(post("/api/reservas").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"clienteId":1,"numeroHabitacion":101,"fechaEntrada":"%s","fechaSalida":"%s"}
                        """.formatted(fecha, fecha)))
                .andExpect(status().isBadRequest());
        crearReserva();
        for (String monto : new String[] {"-1", "null", "0", "1.001", "100000000000"}) {
            mvc.perform(post("/api/reservas/1/confirmacion").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"tarifaBase\":" + monto + "}"))
                    .andExpect(status().isBadRequest());
        }
        assertFalse(Files.exists(archivo));
        mvc.perform(get("/api/reservas/1")).andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void respondeNoEncontradoYConflictoPorDuplicados() throws Exception {
        for (String ruta : new String[] {"clientes", "habitaciones", "reservas"}) {
            mvc.perform(get("/api/" + ruta + "/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.status").value(404));
        }
        mvc.perform(post("/api/reservas").contentType(MediaType.APPLICATION_JSON).content(reservaJson()))
                .andExpect(status().isNotFound());
        registrarDatos();
        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(clienteJson()))
                .andExpect(status().isConflict());
        mvc.perform(get("/api/clientes/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.documentoIdentidad").doesNotExist());
    }

    @Test
    void registraSuiteYValidaSusCampos() throws Exception {
        mvc.perform(post("/api/habitaciones").contentType(MediaType.APPLICATION_JSON).content("""
                {"numero":501,"capacidadMaxima":4,"tipo":"SUITE_PRESIDENCIAL",
                 "amenidadesLujo":["Jacuzzi"],"servicioMayordomo":true}
                """))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.tipo").value("SUITE_PRESIDENCIAL"))
                .andExpect(jsonPath("$.amenidadesLujo[0]").value("Jacuzzi"));
        mvc.perform(post("/api/habitaciones").contentType(MediaType.APPLICATION_JSON).content("""
                {"numero":502,"capacidadMaxima":4,"tipo":"SUITE_PRESIDENCIAL"}
                """))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/habitaciones").contentType(MediaType.APPLICATION_JSON).content("""
                {"numero":503,"capacidadMaxima":4,"tipo":"SUITE_PRESIDENCIAL","amenidadesLujo":[null]}
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void falloDeArchivoNoConfirmaNiOcupaYPermiteReintento() throws Exception {
        registrarDatos();
        crearReserva();
        Files.createDirectory(archivo); // Una carpeta no puede usarse como archivo de confirmaciones.
        mvc.perform(post("/api/reservas/1/confirmacion").contentType(MediaType.APPLICATION_JSON)
                .content("{\"tarifaBase\":100000}"))
                .andExpect(status().isInternalServerError());
        mvc.perform(get("/api/reservas/1"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.total").value(0));
        mvc.perform(get("/api/habitaciones/101"))
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"));
        Files.delete(archivo);
        mvc.perform(post("/api/reservas/1/confirmacion").contentType(MediaType.APPLICATION_JSON)
                .content("{\"tarifaBase\":100000}"))
                .andExpect(status().isOk());
        assertEquals(1, Files.readAllLines(archivo).size());
    }

    private void registrarDatos() throws Exception {
        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON).content(clienteJson()))
                .andExpect(status().isCreated()).andExpect(header().string("Location", "/api/clientes/1"));
        mvc.perform(post("/api/habitaciones").contentType(MediaType.APPLICATION_JSON).content("""
                {"numero":101,"capacidadMaxima":2,"tipo":"ESTANDAR","numeroCamas":2}
                """))
                .andExpect(status().isCreated());
    }

    private void crearReserva() throws Exception {
        mvc.perform(post("/api/reservas").contentType(MediaType.APPLICATION_JSON).content(reservaJson()))
                .andExpect(status().isCreated()).andExpect(header().string("Location", "/api/reservas/1"));
    }

    private String clienteJson() {
        return """
                {"id":1,"nombre":"Ana","documentoIdentidad":"DOC-1",
                 "email":"ana@example.com","telefono":"3001234567"}
                """;
    }

    private String reservaJson() {
        return """
                {"clienteId":1,"numeroHabitacion":101,"fechaEntrada":"%s","fechaSalida":"%s"}
                """.formatted(LocalDate.now().plusDays(10), LocalDate.now().plusDays(12));
    }
}
