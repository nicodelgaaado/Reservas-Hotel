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
import com.reservas.reservas.dto.request.HabitacionRequest;
import com.reservas.reservas.dto.response.HabitacionResponse;
import com.reservas.reservas.mapper.HotelMapper;
import com.reservas.reservas.services.HotelService;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {
    private final HotelService servicio;
    private final HotelMapper mapper;

    public HabitacionController(HotelService servicio, HotelMapper mapper) {
        this.servicio = servicio;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<HabitacionResponse> registrar(@Valid @RequestBody HabitacionRequest request) {
        var response = mapper.toResponse(servicio.registrarHabitacion(mapper.toDomain(request)));
        return ResponseEntity.created(URI.create("/api/habitaciones/" + response.numero())).body(response);
    }

    @GetMapping("/{numero}")
    public HabitacionResponse consultar(@PathVariable int numero) {
        return mapper.toResponse(servicio.consultarHabitacion(numero));
    }
}
