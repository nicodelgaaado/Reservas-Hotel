package com.reservas.reservas.controller;

import java.net.URI;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reservas.reservas.domain.RangoFechas;
import com.reservas.reservas.dto.request.ConfirmarReservaRequest;
import com.reservas.reservas.dto.request.CrearReservaRequest;
import com.reservas.reservas.dto.response.ReservaResponse;
import com.reservas.reservas.mapper.HotelMapper;
import com.reservas.reservas.services.HotelService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    private final HotelService servicio;
    private final HotelMapper mapper;

    public ReservaController(HotelService servicio, HotelMapper mapper) {
        this.servicio = servicio;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody CrearReservaRequest request) {
        var reserva = servicio.crearReserva(request.clienteId(), request.numeroHabitacion(),
                new RangoFechas(request.fechaEntrada(), request.fechaSalida()));
        var response = mapper.toResponse(reserva);
        return ResponseEntity.created(URI.create("/api/reservas/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ReservaResponse consultar(@PathVariable int id) {
        return mapper.toResponse(servicio.consultarReserva(id));
    }

    @PostMapping("/{id}/confirmacion")
    public ReservaResponse confirmar(@PathVariable int id,
            @Valid @RequestBody ConfirmarReservaRequest request) {
        return mapper.toResponse(servicio.confirmarReserva(id, request.tarifaBase().doubleValue()));
    }

    @PostMapping("/{id}/cancelacion")
    public ReservaResponse cancelar(@PathVariable int id) {
        return mapper.toResponse(servicio.cancelarReserva(id));
    }
}
