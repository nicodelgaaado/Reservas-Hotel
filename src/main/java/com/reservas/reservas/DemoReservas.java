package com.reservas.reservas;

import java.time.LocalDate;

import com.reservas.reservas.modelo.*;
import java.util.List;
import com.reservas.reservas.persistencia.ReservaRepository;
import com.reservas.reservas.servicios.ProcesadorReservas;
import com.reservas.reservas.creacionales.builder.PaqueteServicioBuilder;
import com.reservas.reservas.creacionales.factory.*;
import com.reservas.reservas.creacionales.singleton.GeneradorFolioFiscal;
import com.reservas.reservas.estructurales.adapter.*;
import com.reservas.reservas.notificaciones.*;
import com.universidad.reservas.comportamentales.observer.GestorEventosReserva;
import com.universidad.reservas.comportamentales.observer.NotificadorCliente;
import com.universidad.reservas.comportamentales.strategy.CancelacionEstricta;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author Nicolas
 */
@Component
@ConditionalOnProperty(
        name = "reservas.demo.enabled", havingValue = "true", matchIfMissing = true)
public class DemoReservas implements CommandLineRunner {
    private final ProcesadorReservas procesador;
    private final ReservaRepository repositorio;
    private final FacturadorElectronico facturador;
    private final CanalNotificacion notificador;

    public DemoReservas(ProcesadorReservas procesador, ReservaRepository repositorio,
                        FacturadorElectronico facturador, CanalNotificacion notificador) {
        this.procesador = procesador;
        this.repositorio = repositorio;
        this.facturador = facturador;
        this.notificador = notificador;
    }

    @Override
    public void run(String... args) {
        Cliente cliente1 = new Cliente(1, "Nicolas Delgado", "1085123456", "nicolas@example.com", "3001234567");
        GestorEventosReserva eventos = new GestorEventosReserva()
                .suscribir(new NotificadorCliente());
        Habitacion habitacion = new SuitePresidencial(new NumeroHabitacion(501),
                new CapacidadMaxima(4), List.of("Jacuzzi", "Terraza privada"), true);
        ReservaHabitacion reserva1 = new ReservaHabitacion(cliente1, habitacion,
                new RangoFechas(LocalDate.of(2026, 9, 20), LocalDate.of(2026, 9, 23)));
        reserva1.setEstrategiaCancelacion(new CancelacionEstricta());
        reserva1.setGestorEventos(eventos);
        reserva1.agregarPoliticaRecargo(PoliticaRecargoFactory.crear(TipoRecargo.TEMPORADA_ALTA));

        System.out.println(reserva1);
        double tarifaFinal = procesador.confirmarReserva(reserva1, 100000);
        System.out.println("Despues de confirmar: " + reserva1);
        System.out.println("Tarifa final: " + tarifaFinal);
        repositorio.guardarConfirmacion(reserva1, tarifaFinal);

        var paquete = new PaqueteServicioBuilder().incluyeDesayuno().incluyeSpa()
                .incluyeTransporteAeropuerto().numeroPersonas(2).build();
        System.out.println(paquete);
        String folio = GeneradorFolioFiscal.getInstance().generarFolio();
        String factura = facturador.emitirFactura(reserva1, tarifaFinal);
        System.out.println(folio + " - " + factura);

        notificador.notificar(cliente1, "Reserva confirmada");

        reserva1.cancelar(5);
        System.out.println("Después de cancelar: " + reserva1);
    }
}

