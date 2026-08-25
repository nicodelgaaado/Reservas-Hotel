package com.reservas.reservas.modelo;

import java.util.Objects;

import com.reservas.reservas.utilidades.IdRandomizer;

/** Reserva extendida que asocia obligatoriamente una habitación y una estadía. */
public final class ReservaHabitacion extends Reserva {

    private final Habitacion habitacion;
    private final RangoFechas estadia;

    public ReservaHabitacion(Cliente cliente, Habitacion habitacion, RangoFechas estadia) {
        this(IdRandomizer.generar(), cliente, habitacion, estadia);
    }

    public ReservaHabitacion(int id, Cliente cliente, Habitacion habitacion, RangoFechas estadia) {
        super(id, Objects.requireNonNull(cliente, "El cliente es obligatorio"),
                Objects.requireNonNull(estadia, "La estadía es obligatoria").fechaInicio());
        this.habitacion = Objects.requireNonNull(habitacion, "La habitación es obligatoria");
        this.estadia = estadia;
    }

    public Habitacion getHabitacion() { return habitacion; }
    public RangoFechas getEstadia() { return estadia; }
}
