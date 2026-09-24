package com.reservas.reservas.politicas;

import com.reservas.reservas.domain.ReservaHabitacion;

/** Contrato abierto a nuevas reglas de descuento sin cambiar el calculador. */
@FunctionalInterface
public interface PoliticaDescuento {
    double aplicar(ReservaHabitacion reserva, double tarifaActual);
}
