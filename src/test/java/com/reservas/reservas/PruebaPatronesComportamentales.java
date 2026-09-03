package com.reservas.reservas;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import com.reservas.reservas.modelo.CapacidadMaxima;
import com.reservas.reservas.modelo.Cliente;
import com.reservas.reservas.modelo.Habitacion;
import com.reservas.reservas.modelo.NumeroHabitacion;
import com.reservas.reservas.modelo.RangoFechas;
import com.reservas.reservas.modelo.Reserva;
import com.reservas.reservas.modelo.ReservaHabitacion;
import com.universidad.reservas.comportamentales.observer.GestorEventosReserva;
import com.universidad.reservas.comportamentales.observer.GestorHabitaciones;
import com.universidad.reservas.comportamentales.observer.NotificadorCliente;
import com.universidad.reservas.comportamentales.state.EstadoCancelada;
import com.universidad.reservas.comportamentales.strategy.CancelacionEstricta;
import com.universidad.reservas.comportamentales.strategy.CancelacionFlexible;

/** Pruebas ejecutables sin dependencias externas. Requieren la opción -ea. */
public final class PruebaPatronesComportamentales {
    private PruebaPatronesComportamentales() {
    }

    public static void main(String[] args) throws Exception {
        pruebaCancelacionEstrictaYObservadores();
        pruebaUmbralEstrategias();
        pruebaTransicionInvalida();
        System.out.println("Todas las pruebas de patrones comportamentales pasaron.");
    }

    private static void pruebaCancelacionEstrictaYObservadores() throws Exception {
        Cliente cliente = cliente(1);
        Habitacion habitacion = new Habitacion(new NumeroHabitacion(101), new CapacidadMaxima(2));
        ReservaHabitacion reserva = new ReservaHabitacion(100, cliente, habitacion,
                new RangoFechas(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 12)));
        ByteArrayOutputStream correo = new ByteArrayOutputStream();
        GestorEventosReserva eventos = new GestorEventosReserva()
                .suscribir(new GestorHabitaciones())
                .suscribir(new NotificadorCliente(new PrintStream(correo, true, StandardCharsets.UTF_8)));

        reserva.setTotal(200_000);
        reserva.setEstrategiaCancelacion(new CancelacionEstricta());
        reserva.setGestorEventos(eventos);
        reserva.confirmar();
        habitacion.ocupar();
        reserva.cancelar(6);

        assert reserva.getEstado() instanceof EstadoCancelada;
        assert reserva.getMultaCancelacion() == 100_000;
        assert habitacion.estaDisponible();
        assert correo.toString(StandardCharsets.UTF_8).contains(cliente.getEmail());
    }

    private static void pruebaUmbralEstrategias() {
        Reserva reserva = new Reserva(cliente(2), LocalDate.of(2026, 11, 1));
        reserva.setTotal(80_000);
        assert new CancelacionEstricta().calcularMulta(reserva, 7) == 8_000;
        assert new CancelacionFlexible().calcularMulta(reserva, 1) == 0;
    }

    private static void pruebaTransicionInvalida() {
        Reserva reserva = new Reserva(cliente(3), LocalDate.of(2026, 12, 1));
        reserva.confirmar();
        try {
            reserva.confirmar();
            throw new AssertionError("Confirmar dos veces debía fallar");
        } catch (IllegalStateException esperada) {
            assert esperada.getMessage().contains("confirmada");
        }
    }

    private static Cliente cliente(int id) {
        return new Cliente(id, "Cliente " + id, "DOC-" + id,
                "cliente" + id + "@example.com", "3001234567");
    }
}
