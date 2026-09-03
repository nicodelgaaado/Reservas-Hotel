package com.universidad.reservas.comportamentales.observer;

import java.util.Objects;

import com.reservas.reservas.modelo.Reserva;

/** Libera la habitación asociada cuando una reserva confirmada se cancela. */
public final class GestorHabitaciones implements ReservaObserver {
    @Override
    public void onReservaCancelada(Reserva reserva) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria").liberarHabitacion();
    }
}
