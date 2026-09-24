package com.reservas.reservas.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

/** Tarifa base total de la estadia, antes de descuentos y recargos. */
public record ConfirmarReservaRequest(
        @NotNull @DecimalMin("0.01") @Digits(integer = 10, fraction = 2)
        BigDecimal tarifaBase) { }
