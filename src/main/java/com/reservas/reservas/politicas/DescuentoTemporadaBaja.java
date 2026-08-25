package com.reservas.reservas.politicas;

import com.reservas.reservas.modelo.ReservaHabitacion;

/** Descuenta el 15 % de la tarifa sobre la cual se aplica. */
public final class DescuentoTemporadaBaja implements PoliticaDescuento {
    private static final double PORCENTAJE = 0.15;

    @Override
    public double aplicar(ReservaHabitacion reserva, double tarifaActual) {
        return tarifaActual * (1 - PORCENTAJE);
    }
}
