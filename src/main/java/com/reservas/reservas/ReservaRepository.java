package com.reservas.reservas;

/** Abstracción de persistencia para no acoplar la confirmación a un medio concreto. */
@FunctionalInterface
public interface ReservaRepository {
    void guardarConfirmacion(ReservaHabitacion reserva, double tarifaFinal);
}
