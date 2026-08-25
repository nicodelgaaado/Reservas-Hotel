package com.reservas.reservas.persistencia;

import com.reservas.reservas.modelo.ReservaHabitacion;

/** Abstracción de persistencia para no acoplar la confirmación a un medio concreto. */
@FunctionalInterface
public interface ReservaRepository {
    void guardarConfirmacion(ReservaHabitacion reserva, double tarifaFinal);
}
