package com.reservas.reservas.dto.response;

import java.time.LocalDate;

public record ReservaResponse(int id, int clienteId, int numeroHabitacion,
        LocalDate fechaEntrada, LocalDate fechaSalida, String estado,
        double total, double multaCancelacion) { }
