package com.universidad.reservas.comportamentales.strategy;

import com.reservas.reservas.modelo.Reserva;

/** Regla intercambiable para calcular la multa de cancelación. */
@FunctionalInterface
public interface EstrategiaCancelacion {
    double calcularMulta(Reserva reserva, int diasRestantes);
}
