package com.reservas.reservas.servicios;

import java.util.List;
import java.util.Objects;

import com.reservas.reservas.modelo.ReservaHabitacion;
import com.reservas.reservas.politicas.PoliticaDescuento;

/** Aplica una colección de políticas sin conocer sus implementaciones concretas. */
public final class CalculadorTarifa {
    private final List<PoliticaDescuento> politicas;

    public CalculadorTarifa(List<PoliticaDescuento> politicas) {
        this.politicas = List.copyOf(Objects.requireNonNull(politicas, "Las políticas son obligatorias"));
    }

    public double calcular(ReservaHabitacion reserva, double tarifaBase) {
        if (tarifaBase < 0) {
            throw new IllegalArgumentException("La tarifa base no puede ser negativa");
        }
        double tarifa = tarifaBase;
        for (PoliticaDescuento politica : politicas) {
            tarifa = Objects.requireNonNull(politica, "La política no puede ser nula").aplicar(reserva, tarifa);
        }
        return tarifa;
    }
}
