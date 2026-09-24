package com.reservas.reservas.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrearReservaRequest(
        @Positive int clienteId,
        @Positive int numeroHabitacion,
        @NotNull @FutureOrPresent LocalDate fechaEntrada,
        @NotNull LocalDate fechaSalida) { }
