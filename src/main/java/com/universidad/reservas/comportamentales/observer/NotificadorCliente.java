package com.universidad.reservas.comportamentales.observer;

import java.io.PrintStream;
import java.util.Objects;

import com.reservas.reservas.modelo.Reserva;

/** Simula el envío de un correo al cliente cuya reserva fue cancelada. */
public final class NotificadorCliente implements ReservaObserver {
    private final PrintStream salida;

    public NotificadorCliente() {
        this(System.out);
    }

    public NotificadorCliente(PrintStream salida) {
        this.salida = Objects.requireNonNull(salida, "La salida es obligatoria");
    }

    @Override
    public void onReservaCancelada(Reserva reserva) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria");
        salida.printf("[EMAIL %s] Reserva %d cancelada. Multa: %.2f%n",
                reserva.getCliente().getEmail(), reserva.getId(), reserva.getMultaCancelacion());
    }
}
