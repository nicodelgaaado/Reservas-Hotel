package com.reservas.reservas.repository;

import java.util.Optional;
import com.reservas.reservas.domain.Cliente;
import com.reservas.reservas.domain.Habitacion;
import com.reservas.reservas.domain.ReservaHabitacion;

/** Almacen de entidades vivas; distinto del registro de confirmaciones en archivo. */
public interface HotelRepository {
    void agregarCliente(Cliente cliente);
    void agregarHabitacion(Habitacion habitacion);
    void agregarReserva(ReservaHabitacion reserva);
    Optional<Cliente> buscarCliente(int id);
    Optional<Habitacion> buscarHabitacion(int numero);
    Optional<ReservaHabitacion> buscarReserva(int id);
    int siguienteIdReserva();
}
