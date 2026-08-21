package com.reservas.reservas;

/** Descuenta el 25 % cuando la estadía supera siete días. */
public final class DescuentoEstadiaLarga implements PoliticaDescuento {
    private static final long DIAS_MINIMOS = 7;
    private static final double PORCENTAJE = 0.25;

    @Override
    public double aplicar(ReservaHabitacion reserva, double tarifaActual) {
        if (reserva.getEstadia().duracionEnDias() > DIAS_MINIMOS) {
            return tarifaActual * (1 - PORCENTAJE);
        }
        return tarifaActual;
    }
}
