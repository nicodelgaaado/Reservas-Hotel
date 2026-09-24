package com.reservas.reservas.dto.response;

import java.util.List;

public record HabitacionResponse(int numero, int capacidadMaxima, String tipo,
        String estado, Integer numeroCamas, List<String> amenidadesLujo,
        boolean servicioMayordomo) {
    public HabitacionResponse {
        amenidadesLujo = List.copyOf(amenidadesLujo);
    }
}
