package com.reservas.reservas.modelo;

import java.util.Objects;

/** Entidad del dominio identificada por su número de habitación. */
public final class Habitacion {

    private final NumeroHabitacion numero;
    private final CapacidadMaxima capacidadMaxima;
    private EstadoHabitacion estado;

    public Habitacion(NumeroHabitacion numero, CapacidadMaxima capacidadMaxima) {
        this(numero, capacidadMaxima, EstadoHabitacion.DISPONIBLE);
    }

    public Habitacion(NumeroHabitacion numero, CapacidadMaxima capacidadMaxima, EstadoHabitacion estado) {
        this.numero = Objects.requireNonNull(numero, "El número de habitación es obligatorio");
        this.capacidadMaxima = Objects.requireNonNull(capacidadMaxima, "La capacidad máxima es obligatoria");
        this.estado = Objects.requireNonNull(estado, "El estado de la habitación es obligatorio");
    }

    public NumeroHabitacion getNumero() { return numero; }
    public CapacidadMaxima getCapacidadMaxima() { return capacidadMaxima; }
    public EstadoHabitacion getEstado() { return estado; }

    public boolean estaDisponible() { return estado == EstadoHabitacion.DISPONIBLE; }

    public void ocupar() {
        if (!estaDisponible()) {
            throw new IllegalStateException("La habitación " + numero + " no está disponible");
        }
        estado = EstadoHabitacion.OCUPADA;
    }

    public void liberar() {
        if (estado == EstadoHabitacion.MANTENIMIENTO) {
            throw new IllegalStateException("No se puede liberar una habitación en mantenimiento");
        }
        estado = EstadoHabitacion.DISPONIBLE;
    }

    public void ponerEnMantenimiento() {
        if (estado == EstadoHabitacion.OCUPADA) {
            throw new IllegalStateException("No se puede poner en mantenimiento una habitación ocupada");
        }
        estado = EstadoHabitacion.MANTENIMIENTO;
    }

    @Override
    public boolean equals(Object objeto) {
        return objeto instanceof Habitacion otra && numero.equals(otra.numero);
    }

    @Override
    public int hashCode() { return numero.hashCode(); }
}
