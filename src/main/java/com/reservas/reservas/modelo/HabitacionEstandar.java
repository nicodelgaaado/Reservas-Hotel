package com.reservas.reservas.modelo;

public final class HabitacionEstandar extends Habitacion {
    private final int numeroCamas;

    public HabitacionEstandar(NumeroHabitacion numero, CapacidadMaxima capacidadMaxima, int numeroCamas) {
        this(numero, capacidadMaxima, numeroCamas, EstadoHabitacion.DISPONIBLE);
    }

    public HabitacionEstandar(NumeroHabitacion numero, CapacidadMaxima capacidadMaxima,
                             int numeroCamas, EstadoHabitacion estado) {
        super(numero, capacidadMaxima, estado);
        if (numeroCamas < 1) {
            throw new IllegalArgumentException("El número de camas debe ser positivo");
        }
        this.numeroCamas = numeroCamas;
    }

    public int getNumeroCamas() { return numeroCamas; }
}
