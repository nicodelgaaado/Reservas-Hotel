package com.reservas.reservas.persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import com.reservas.reservas.modelo.ReservaHabitacion;

/** Repositorio que añade una línea por cada reserva confirmada a un archivo plano. */
public final class ReservaArchivoRepository implements ReservaRepository {
    private final Path archivo;

    public ReservaArchivoRepository(Path archivo) {
        this.archivo = Objects.requireNonNull(archivo, "El archivo es obligatorio");
    }

    @Override
    public void guardarConfirmacion(ReservaHabitacion reserva, double tarifaFinal) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria");
        String linea = "%d|%s|habitacion=%s|%s|%s|tarifaFinal=%.2f%n".formatted(
                reserva.getId(), reserva.getCliente().getNombre(), reserva.getHabitacion().getNumero(),
                reserva.getEstadia().fechaInicio(), reserva.getEstadia().fechaFin(), tarifaFinal);
        try {
            Path directorio = archivo.toAbsolutePath().getParent();
            if (directorio != null) {
                Files.createDirectories(directorio);
            }
            Files.writeString(archivo, linea, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("No fue posible guardar la confirmación", e);
        }
    }
}
