package com.universidad.reservas.comportamentales.observer;

import com.reservas.reservas.modelo.Reserva;

/** Suscriptor de eventos relevantes de una reserva. */
@FunctionalInterface
public interface ReservaObserver {
    void onReservaCancelada(Reserva reserva);
}
