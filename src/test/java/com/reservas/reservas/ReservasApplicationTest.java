package com.reservas.reservas;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.reservas.reservas.domain.*;
import com.reservas.reservas.services.ServicioConfirmacionReservas;
import com.universidad.reservas.comportamentales.observer.GestorEventosReserva;
import com.universidad.reservas.comportamentales.observer.GestorHabitaciones;
import com.universidad.reservas.comportamentales.state.EstadoConfirmada;

import static org.junit.jupiter.api.Assertions.*;

class ReservasApplicationTest {
    @TempDir
    Path directorio;

    @Test
    void inyectaServiciosConfirmaPersisteYLiberaHabitacion() throws Exception {
        Path archivo = directorio.resolve("confirmaciones.txt");
        try (var contexto = new SpringApplicationBuilder(Reservas.class)
                .web(WebApplicationType.NONE)
                .run("--reservas.demo.enabled=false", "--reservas.archivo=" + archivo)) {
            assertTrue(contexto.getBeansOfType(DemoReservas.class).isEmpty());
            assertFalse(Files.exists(archivo));
            var cliente = new Cliente(1, "Ana", "DOC-1", "ana@example.com", "3001234567");
            var habitacion = new HabitacionEstandar(new NumeroHabitacion(101), new CapacidadMaxima(2), 2);
            var reserva = new ReservaHabitacion(123, cliente, habitacion,
                    new RangoFechas(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 3)));
            reserva.setGestorEventos(new GestorEventosReserva().suscribir(new GestorHabitaciones()));
            double esperado = 100_000 * (1 - cliente.getMembresia().getPorcentajeDescuento());
            var servicio = contexto.getBean(ServicioConfirmacionReservas.class);
            assertEquals(esperado, servicio.confirmar(reserva, 100_000), 0.001);
            assertInstanceOf(EstadoConfirmada.class, reserva.getEstado());
            assertFalse(habitacion.estaDisponible());
            assertTrue(Files.readString(archivo).contains("123|Ana|habitacion=101|"));
            assertThrows(IllegalStateException.class, () -> servicio.confirmar(reserva, 100_000));
            assertEquals(1, Files.readAllLines(archivo).size());
            reserva.cancelar(5);
            assertTrue(habitacion.estaDisponible());
        }
    }

    @Test
    void ejecutaDemoAlArrancar() throws Exception {
        Path archivo = directorio.resolve("demo.txt");
        try (var contexto = new SpringApplicationBuilder(Reservas.class)
                .web(WebApplicationType.NONE)
                .run("--reservas.demo.enabled=true", "--reservas.archivo=" + archivo)) {
            assertNotNull(contexto.getBean(DemoReservas.class));
            assertEquals(1, Files.readAllLines(archivo).size());
        }
    }
}
