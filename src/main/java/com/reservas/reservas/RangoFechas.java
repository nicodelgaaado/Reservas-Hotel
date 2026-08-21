package com.reservas.reservas;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/** Intervalo inmutable de una estadía. La fecha de salida no se incluye. */
public record RangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {

    public RangoFechas {
        Objects.requireNonNull(fechaInicio, "La fecha de inicio es obligatoria");
        Objects.requireNonNull(fechaFin, "La fecha de fin es obligatoria");
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    public long duracionEnDias() {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }
}
