package com.reservas.reservas.servicios;

import com.reservas.reservas.modelo.Cliente;
import com.reservas.reservas.modelo.Reserva;
import com.universidad.reservas.comportamentales.state.EstadoPendiente;

public class ProcesadorReservas {

    private int reservasConfirmadas;
    private double totalFacturado;

    public double confirmarReserva(Reserva reserva, double tarifaBase) {
        double tarifaFinal = calcularTarifaFinal(reserva, tarifaBase);
        reserva.setTotal(tarifaFinal);
        reserva.confirmar();
        reservasConfirmadas++;
        totalFacturado += tarifaFinal;
        return tarifaFinal;
    }

    public double calcularTarifaFinal(Reserva reserva, double tarifaBase) {
        validarReserva(reserva, tarifaBase);
        double descuento = reserva.getCliente().getMembresia().getPorcentajeDescuento();
        return tarifaBase * (1 - descuento);
    }

    public boolean puedeConfirmar(Reserva reserva) {
        return reserva != null && reserva.getCliente() != null
                && !reserva.getCliente().estaBloqueado()
                && reserva.getEstado() instanceof EstadoPendiente;
    }

    public int getReservasConfirmadas() { return reservasConfirmadas; }

    public double getTotalFacturado() { return totalFacturado; }

    private void validarReserva(Reserva reserva, double tarifaBase) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }
        if (tarifaBase < 0) {
            throw new IllegalArgumentException("La tarifa base no puede ser negativa");
        }

        Cliente cliente = reserva.getCliente();
        if (cliente == null) {
            throw new IllegalArgumentException("La reserva debe tener un cliente");
        }
        if (cliente.estaBloqueado()) {
            throw new IllegalStateException(
                    "No se puede confirmar la reserva: el cliente está bloqueado");
        }
        if (!(reserva.getEstado() instanceof EstadoPendiente)) {
            throw new IllegalStateException("Solo se pueden confirmar reservas pendientes");
        }
    }

    @Override
    public String toString() {
        return "ProcesadorReservas{reservasConfirmadas=" + reservasConfirmadas
                + ", totalFacturado=" + totalFacturado + "}";
    }
}
