package com.universidad.reservas.comportamentales.strategy;

import java.util.Objects;

import com.reservas.reservas.modelo.Reserva;

/** Política sin penalización por cancelación. */
public final class CancelacionFlexible implements EstrategiaCancelacion {
    @Override
    public double calcularMulta(Reserva reserva, int diasRestantes) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria");
        return 0.0;
    }
}
