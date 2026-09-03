package com.universidad.reservas.comportamentales.state;

import java.util.Objects;

import com.reservas.reservas.modelo.Reserva;

/** Permite confirmar o cancelar una reserva que aún no ha sido confirmada. */
public final class EstadoPendiente implements EstadoReserva {
    @Override
    public void confirmar(Reserva contexto) {
        Objects.requireNonNull(contexto, "La reserva es obligatoria")
                .cambiarEstado(new EstadoConfirmada());
    }

    @Override
    public void cancelar(Reserva contexto, int diasRestantes) {
        Objects.requireNonNull(contexto, "La reserva es obligatoria");
        contexto.registrarMultaCancelacion(0.0);
        contexto.cambiarEstado(new EstadoCancelada());
    }

    @Override
    public String toString() {
        return "PENDIENTE";
    }
}
