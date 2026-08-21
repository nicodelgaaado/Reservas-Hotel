package com.reservas.reservas;

/** Contrato abierto a nuevas reglas de descuento sin cambiar el calculador. */
@FunctionalInterface
public interface PoliticaDescuento {
    double aplicar(ReservaHabitacion reserva, double tarifaActual);
}
