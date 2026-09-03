package com.universidad.reservas.comportamentales.observer;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import com.reservas.reservas.modelo.Reserva;

/** Publicador que administra y notifica observadores de reservas. */
public final class GestorEventosReserva {
    private final List<ReservaObserver> suscriptores = new CopyOnWriteArrayList<>();

    public GestorEventosReserva suscribir(ReservaObserver observador) {
        suscriptores.add(Objects.requireNonNull(observador, "El observador es obligatorio"));
        return this;
    }

    public void desuscribir(ReservaObserver observador) {
        suscriptores.remove(Objects.requireNonNull(observador, "El observador es obligatorio"));
    }

    public void notificarCancelacion(Reserva reserva) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria");
        suscriptores.forEach(observador -> observador.onReservaCancelada(reserva));
    }

    public int numeroSuscriptores() {
        return suscriptores.size();
    }
}
