package com.universidad.reservas.comportamentales.strategy;

import java.util.Objects;

import com.reservas.reservas.modelo.Reserva;

/** Penaliza con 50 % a menos de siete días y con 10 % en los demás casos. */
public final class CancelacionEstricta implements EstrategiaCancelacion {
    private static final int LIMITE_CANCELACION_TARDIA = 7;
    private static final double PORCENTAJE_TARDIO = 0.50;
    private static final double PORCENTAJE_ANTICIPADO = 0.10;

    @Override
    public double calcularMulta(Reserva reserva, int diasRestantes) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria");
        if (diasRestantes < 0) {
            throw new IllegalArgumentException("Los días restantes no pueden ser negativos");
        }
        double porcentaje = diasRestantes < LIMITE_CANCELACION_TARDIA
                ? PORCENTAJE_TARDIO
                : PORCENTAJE_ANTICIPADO;
        return reserva.getTotal() * porcentaje;
    }
}
