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
import com.reservas.reservas.dto.request.ClienteRequest;
import com.reservas.reservas.dto.response.ClienteResponse;
import com.reservas.reservas.mapper.HotelMapper;
import com.reservas.reservas.services.HotelService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final HotelService servicio;
    private final HotelMapper mapper;

    public ClienteController(HotelService servicio, HotelMapper mapper) {
        this.servicio = servicio;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> registrar(@Valid @RequestBody ClienteRequest request) {
        var response = mapper.toResponse(servicio.registrarCliente(mapper.toDomain(request)));
        return ResponseEntity.created(URI.create("/api/clientes/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ClienteResponse consultar(@PathVariable int id) {
        return mapper.toResponse(servicio.consultarCliente(id));
    }
}
