package com.reservas.reservas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @Positive int id,
        @NotBlank @Size(max = 120) @Pattern(regexp = "[^|\\r\\n]+") String nombre,
        @NotBlank @Size(max = 40) String documentoIdentidad,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(max = 30) String telefono) { }
