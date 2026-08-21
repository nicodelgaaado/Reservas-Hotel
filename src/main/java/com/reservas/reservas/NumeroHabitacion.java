package com.reservas.reservas;

/** Identificador inmutable de una habitación. */
public record NumeroHabitacion(int valor) {

    public NumeroHabitacion {
        if (valor < 1) {
            throw new IllegalArgumentException("El número de habitación debe ser mayor que cero");
        }
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}
