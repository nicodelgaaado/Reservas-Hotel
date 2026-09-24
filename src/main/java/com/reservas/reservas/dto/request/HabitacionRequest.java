package com.reservas.reservas.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record HabitacionRequest(
        @Positive int numero,
        @Positive int capacidadMaxima,
        @NotNull Tipo tipo,
        @Positive Integer numeroCamas,
        @Size(max = 30) List<@NotBlank @Size(max = 120) String> amenidadesLujo,
        Boolean servicioMayordomo) {
    public enum Tipo { ESTANDAR, SUITE_PRESIDENCIAL }
}
