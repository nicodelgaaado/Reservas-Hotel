package com.universidad.reservas.comportamentales.state;

import java.util.Objects;

import com.reservas.reservas.modelo.Reserva;

/** Estado terminal de una reserva. */
public final class EstadoCancelada implements EstadoReserva {
    @Override
    public void confirmar(Reserva contexto) {
        Objects.requireNonNull(contexto, "La reserva es obligatoria");
        throw new IllegalStateException("No se puede confirmar una reserva cancelada");
    }

    @Override
    public void cancelar(Reserva contexto, int diasRestantes) {
        Objects.requireNonNull(contexto, "La reserva es obligatoria");
        throw new IllegalStateException("La reserva ya está cancelada");
    }

    @Override
    public String toString() {
        return "CANCELADA";
    }
}
