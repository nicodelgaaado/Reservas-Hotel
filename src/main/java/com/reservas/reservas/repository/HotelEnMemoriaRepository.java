package com.reservas.reservas.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import com.reservas.reservas.domain.Cliente;
import com.reservas.reservas.domain.Habitacion;
import com.reservas.reservas.domain.ReservaHabitacion;

/** Los datos duran lo que dura el proceso; no reconstruye reservas desde el archivo. */
public final class HotelEnMemoriaRepository implements HotelRepository {
    private final Map<Integer, Cliente> clientes = new ConcurrentHashMap<>();
    private final Map<Integer, Habitacion> habitaciones = new ConcurrentHashMap<>();
    private final Map<Integer, ReservaHabitacion> reservas = new ConcurrentHashMap<>();
    private final AtomicInteger secuenciaReservas = new AtomicInteger();

    @Override
    public void agregarCliente(Cliente cliente) {
        if (clientes.putIfAbsent(cliente.getId(), cliente) != null) {
            throw new IllegalStateException("El cliente ya existe");
        }
    }

    @Override
    public void agregarHabitacion(Habitacion habitacion) {
        if (habitaciones.putIfAbsent(habitacion.getNumero().valor(), habitacion) != null) {
            throw new IllegalStateException("La habitacion ya existe");
        }
    }

    @Override
    public void agregarReserva(ReservaHabitacion reserva) {
        if (reservas.putIfAbsent(reserva.getId(), reserva) != null) {
            throw new IllegalStateException("La reserva ya existe");
        }
    }

    @Override
    public Optional<Cliente> buscarCliente(int id) { return Optional.ofNullable(clientes.get(id)); }

    @Override
    public Optional<Habitacion> buscarHabitacion(int numero) {
        return Optional.ofNullable(habitaciones.get(numero));
    }

    @Override
    public Optional<ReservaHabitacion> buscarReserva(int id) {
        return Optional.ofNullable(reservas.get(id));
    }

    @Override
    public int siguienteIdReserva() { return secuenciaReservas.incrementAndGet(); }
}
