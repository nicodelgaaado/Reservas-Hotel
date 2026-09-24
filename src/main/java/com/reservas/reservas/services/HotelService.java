package com.reservas.reservas.services;

import com.reservas.reservas.domain.Cliente;
import com.reservas.reservas.domain.Habitacion;
import com.reservas.reservas.domain.RangoFechas;
import com.reservas.reservas.domain.ReservaHabitacion;
import com.reservas.reservas.repository.HotelRepository;
import com.universidad.reservas.comportamentales.observer.GestorEventosReserva;
import com.universidad.reservas.comportamentales.observer.GestorHabitaciones;
import com.universidad.reservas.comportamentales.state.EstadoConfirmada;

/**
 * Coordina las operaciones de la API sobre las mismas entidades del dominio.
 * Serializa las mutaciones para proteger la disponibilidad en una sola instancia.
 */
public final class HotelService {
    private final HotelRepository repositorio;
    private final ServicioConfirmacionReservas confirmaciones;

    public HotelService(HotelRepository repositorio, ServicioConfirmacionReservas confirmaciones) {
        this.repositorio = repositorio;
        this.confirmaciones = confirmaciones;
    }

    public synchronized Cliente registrarCliente(Cliente cliente) {
        repositorio.agregarCliente(cliente);
        return cliente;
    }

    public synchronized Habitacion registrarHabitacion(Habitacion habitacion) {
        repositorio.agregarHabitacion(habitacion);
        return habitacion;
    }

    public synchronized Cliente consultarCliente(int id) {
        return repositorio.buscarCliente(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + id));
    }

    public synchronized Habitacion consultarHabitacion(int numero) {
        return repositorio.buscarHabitacion(numero)
                .orElseThrow(() -> new RecursoNoEncontradoException("Habitacion no encontrada: " + numero));
    }

    public synchronized ReservaHabitacion consultarReserva(int id) {
        return repositorio.buscarReserva(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada: " + id));
    }

    public synchronized ReservaHabitacion crearReserva(int clienteId, int numero, RangoFechas estadia) {
        var cliente = consultarCliente(clienteId);
        var habitacion = consultarHabitacion(numero);
        if (cliente.estaBloqueado()) {
            throw new IllegalStateException("El cliente esta bloqueado");
        }
        if (!habitacion.estaDisponible()) {
            throw new IllegalStateException("La habitacion no esta disponible");
        }
        var reserva = new ReservaHabitacion(repositorio.siguienteIdReserva(), cliente, habitacion, estadia);
        // Solo una reserva confirmada puede liberar una habitacion al cancelar.
        reserva.setGestorEventos(new GestorEventosReserva().suscribir(new GestorHabitaciones()));
        repositorio.agregarReserva(reserva);
        return reserva;
    }

    public synchronized ReservaHabitacion confirmarReserva(int id, double tarifaBase) {
        var reserva = consultarReserva(id);
        confirmaciones.confirmar(reserva, tarifaBase);
        return reserva;
    }

    public synchronized ReservaHabitacion cancelarReserva(int id) {
        var reserva = consultarReserva(id);
        if (!(reserva.getEstado() instanceof EstadoConfirmada)) {
            throw new IllegalStateException("Solo se pueden cancelar reservas confirmadas");
        }
        reserva.cancelar();
        return reserva;
    }
}
