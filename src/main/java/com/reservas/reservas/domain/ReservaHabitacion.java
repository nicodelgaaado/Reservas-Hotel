package com.reservas.reservas.domain;

import java.util.Objects;

import com.reservas.reservas.utilidades.IdRandomizer;

/** Reserva extendida que asocia obligatoriamente una habitación y una estadía. */
public final class ReservaHabitacion extends Reserva {

    private final Habitacion habitacion;

    public ReservaHabitacion(Cliente cliente, Habitacion habitacion, RangoFechas estadia) {
        this(IdRandomizer.generar(), cliente, habitacion, estadia);
    }

    public ReservaHabitacion(int id, Cliente cliente, Habitacion habitacion, RangoFechas estadia) {
        super(id, Objects.requireNonNull(cliente, "El cliente es obligatorio"),
                Objects.requireNonNull(estadia, "La estadía es obligatoria"));
        this.habitacion = Objects.requireNonNull(habitacion, "La habitación es obligatoria");
        habitacion.registrarReserva(this);
    }

    public Habitacion getHabitacion() { return habitacion; }

    @Override
    public void liberarHabitacion() {
        habitacion.liberar();
    }
}
