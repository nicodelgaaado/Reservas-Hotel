package com.reservas.reservas.modelo;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

/** Entidad del dominio identificada por su número de habitación. */
public abstract class Habitacion {

    private final NumeroHabitacion numero;
    private final CapacidadMaxima capacidadMaxima;
    private EstadoHabitacion estado;
    private final List<Reserva> historialReservas = new ArrayList<>();

    protected Habitacion(NumeroHabitacion numero, CapacidadMaxima capacidadMaxima) {
        this(numero, capacidadMaxima, EstadoHabitacion.DISPONIBLE);
    }

    protected Habitacion(NumeroHabitacion numero, CapacidadMaxima capacidadMaxima, EstadoHabitacion estado) {
        this.numero = Objects.requireNonNull(numero, "El número de habitación es obligatorio");
        this.capacidadMaxima = Objects.requireNonNull(capacidadMaxima, "La capacidad máxima es obligatoria");
        this.estado = Objects.requireNonNull(estado, "El estado de la habitación es obligatorio");
    }

    public NumeroHabitacion getNumero() { return numero; }
    public CapacidadMaxima getCapacidadMaxima() { return capacidadMaxima; }
    public EstadoHabitacion getEstado() { return estado; }

    public List<Reserva> getHistorialReservas() { return List.copyOf(historialReservas); }

    /** Solo la asociación de dominio registra reservas; cancelar no borra el historial. */
    void registrarReserva(ReservaHabitacion reserva) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria");
        if (reserva.getHabitacion() != this) {
            throw new IllegalArgumentException("La reserva pertenece a otra habitación");
        }
        if (historialReservas.stream().noneMatch(existente -> existente == reserva)) {
            historialReservas.add(reserva);
        }
    }

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
