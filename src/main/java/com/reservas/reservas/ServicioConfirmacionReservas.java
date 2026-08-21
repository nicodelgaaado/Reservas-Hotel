package com.reservas.reservas;

import java.util.List;
import java.util.Objects;

/** Caso de uso que depende de contratos, no de políticas, canales o archivos concretos. */
public final class ServicioConfirmacionReservas {
    private final CalculadorTarifa calculadorTarifa;
    private final ReservaRepository repositorio;
    private final List<CanalNotificacion> canales;

    public ServicioConfirmacionReservas(CalculadorTarifa calculadorTarifa,
                                        ReservaRepository repositorio,
                                        List<CanalNotificacion> canales) {
        this.calculadorTarifa = Objects.requireNonNull(calculadorTarifa, "El calculador es obligatorio");
        this.repositorio = Objects.requireNonNull(repositorio, "El repositorio es obligatorio");
        this.canales = List.copyOf(Objects.requireNonNull(canales, "Los canales son obligatorios"));
    }

    public double confirmar(ReservaHabitacion reserva, double tarifaBase) {
        validar(reserva);
        double tarifaFinal = calculadorTarifa.calcular(reserva, tarifaBase);
        reserva.confirmar();
        reserva.getHabitacion().ocupar();
        repositorio.guardarConfirmacion(reserva, tarifaFinal);

        String mensaje = "Reserva " + reserva.getId() + " confirmada. Tarifa final: " + tarifaFinal;
        for (CanalNotificacion canal : canales) {
            Objects.requireNonNull(canal, "El canal no puede ser nulo").notificar(reserva.getCliente(), mensaje);
        }
        return tarifaFinal;
    }

    private void validar(ReservaHabitacion reserva) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria");
        if (reserva.getCliente().estaBloqueado()) {
            throw new IllegalStateException("No se puede confirmar la reserva: el cliente está bloqueado");
        }
        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden confirmar reservas pendientes");
        }
        if (!reserva.getHabitacion().estaDisponible()) {
            throw new IllegalStateException("La habitación no está disponible");
        }
    }
}
