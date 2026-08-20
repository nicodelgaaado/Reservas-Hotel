package com.reservas.reservas;

import java.time.LocalDate;
import java.util.Objects;

public class Reserva {

    private int id;
    private Cliente cliente;
    private LocalDate fechaInicio;
    private EstadoReserva estado;

    public Reserva(Cliente cliente, LocalDate fechaInicio) {
        this(IdRandomizer.generar(), cliente, fechaInicio);
    }

    public Reserva(int id, Cliente cliente, LocalDate fechaInicio) {
        this.id = id;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.estado = EstadoReserva.PENDIENTE; // estado inicial por defecto
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }

    public void confirmar() { this.estado = EstadoReserva.CONFIRMADA; }
    public void cancelar() { this.estado = EstadoReserva.CANCELADA; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        Reserva reserva = (Reserva) o;
        return id == reserva.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Reserva{id=" + id + ", cliente=" + cliente.getNombre()
                + ", fechaInicio=" + fechaInicio + ", estado=" + estado + "}";
    }
}
