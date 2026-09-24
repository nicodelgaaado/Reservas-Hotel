package com.universidad.reservas.comportamentales.strategy;

import com.reservas.reservas.domain.Reserva;

/** Regla intercambiable para calcular la multa de cancelación. */
@FunctionalInterface
public interface EstrategiaCancelacion {
    double calcularMulta(Reserva reserva, int diasRestantes);
}
