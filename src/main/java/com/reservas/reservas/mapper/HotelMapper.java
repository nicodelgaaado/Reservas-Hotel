package com.reservas.reservas.mapper;

import java.util.List;
import com.reservas.reservas.domain.CapacidadMaxima;
import com.reservas.reservas.domain.Cliente;
import com.reservas.reservas.domain.Habitacion;
import com.reservas.reservas.domain.HabitacionEstandar;
import com.reservas.reservas.domain.NumeroHabitacion;
import com.reservas.reservas.domain.ReservaHabitacion;
import com.reservas.reservas.domain.SuitePresidencial;
import com.reservas.reservas.dto.request.ClienteRequest;
import com.reservas.reservas.dto.request.HabitacionRequest;
import com.reservas.reservas.dto.response.ClienteResponse;
import com.reservas.reservas.dto.response.HabitacionResponse;
import com.reservas.reservas.dto.response.ReservaResponse;

/** Conversiones explicitas, sin persistencia ni ejecucion de casos de uso. */
public final class HotelMapper {
    public Cliente toDomain(ClienteRequest request) {
        return new Cliente(request.id(), request.nombre(), request.documentoIdentidad(),
                request.email(), request.telefono());
    }

    public Habitacion toDomain(HabitacionRequest request) {
        var numero = new NumeroHabitacion(request.numero());
        var capacidad = new CapacidadMaxima(request.capacidadMaxima());
        return switch (request.tipo()) {
            case ESTANDAR -> {
                if (request.numeroCamas() == null) {
                    throw new IllegalArgumentException("Una habitacion estandar requiere numeroCamas");
                }
                if (Boolean.TRUE.equals(request.servicioMayordomo())
                        || (request.amenidadesLujo() != null && !request.amenidadesLujo().isEmpty())) {
                    throw new IllegalArgumentException("Los servicios de lujo corresponden a una suite");
                }
                yield new HabitacionEstandar(numero, capacidad, request.numeroCamas());
            }
            case SUITE_PRESIDENCIAL -> {
                if (request.amenidadesLujo() == null || request.amenidadesLujo().isEmpty()) {
                    throw new IllegalArgumentException("Una suite requiere amenidadesLujo");
                }
                if (request.numeroCamas() != null) {
                    throw new IllegalArgumentException("numeroCamas corresponde a una habitacion estandar");
                }
                yield new SuitePresidencial(numero, capacidad, request.amenidadesLujo(),
                        Boolean.TRUE.equals(request.servicioMayordomo()));
            }
        };
    }

    public ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNombre(), cliente.getEmail(),
                cliente.getMembresia().name());
    }

    public HabitacionResponse toResponse(Habitacion habitacion) {
        if (habitacion instanceof SuitePresidencial suite) {
            return new HabitacionResponse(suite.getNumero().valor(), suite.getCapacidadMaxima().valor(),
                    "SUITE_PRESIDENCIAL", suite.getEstado().name(), null,
                    suite.getAmenidadesLujo(), suite.tieneServicioMayordomo());
        }
        var estandar = (HabitacionEstandar) habitacion;
        return new HabitacionResponse(estandar.getNumero().valor(), estandar.getCapacidadMaxima().valor(),
                "ESTANDAR", estandar.getEstado().name(), estandar.getNumeroCamas(), List.of(), false);
    }

    public ReservaResponse toResponse(ReservaHabitacion reserva) {
        return new ReservaResponse(reserva.getId(), reserva.getCliente().getId(),
                reserva.getHabitacion().getNumero().valor(), reserva.getFechaInicio(),
                reserva.getFechaFin(), reserva.getEstado().toString(), reserva.getTotal(),
                reserva.getMultaCancelacion());
    }
}
