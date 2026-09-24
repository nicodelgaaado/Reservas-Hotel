package com.reservas.reservas.domain;

import java.util.List;
import java.util.Objects;

public final class SuitePresidencial extends Habitacion {
    private final List<String> amenidadesLujo;
    private final boolean servicioMayordomo;

    public SuitePresidencial(NumeroHabitacion numero, CapacidadMaxima capacidadMaxima,
                            List<String> amenidadesLujo, boolean servicioMayordomo) {
        this(numero, capacidadMaxima, amenidadesLujo, servicioMayordomo, EstadoHabitacion.DISPONIBLE);
    }

    public SuitePresidencial(NumeroHabitacion numero, CapacidadMaxima capacidadMaxima,
                            List<String> amenidadesLujo, boolean servicioMayordomo, EstadoHabitacion estado) {
        super(numero, capacidadMaxima, estado);
        this.amenidadesLujo = List.copyOf(Objects.requireNonNull(amenidadesLujo,
                "Las amenidades son obligatorias"));
        if (this.amenidadesLujo.isEmpty() || this.amenidadesLujo.stream()
                .anyMatch(amenidad -> Objects.requireNonNull(amenidad).isBlank())) {
            throw new IllegalArgumentException("Debe indicar amenidades de lujo no vacías");
        }
        this.servicioMayordomo = servicioMayordomo;
    }

    public List<String> getAmenidadesLujo() { return amenidadesLujo; }
    public boolean tieneServicioMayordomo() { return servicioMayordomo; }
}
