package com.reservas.reservas.modelo;

/** Cantidad máxima, inmutable y válida, de huéspedes de una habitación. */
public record CapacidadMaxima(int valor) {

    public CapacidadMaxima {
        if (valor < 1) {
            throw new IllegalArgumentException("La capacidad máxima debe ser de al menos una persona");
        }
    }
}
