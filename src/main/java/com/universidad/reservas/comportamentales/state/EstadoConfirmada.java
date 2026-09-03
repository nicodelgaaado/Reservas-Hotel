package com.universidad.reservas.comportamentales.state;

import java.util.Objects;

import com.reservas.reservas.modelo.Reserva;

/** Estado que permite cancelar aplicando la estrategia y publicando el evento. */
public final class EstadoConfirmada implements EstadoReserva {
    @Override
    public void confirmar(Reserva contexto) {
        Objects.requireNonNull(contexto, "La reserva es obligatoria");
        throw new IllegalStateException("La reserva ya está confirmada");
    }

    @Override
    public void cancelar(Reserva contexto, int diasRestantes) {
        Objects.requireNonNull(contexto, "La reserva es obligatoria");
        double multa = contexto.getEstrategiaCancelacion()
                .calcularMulta(contexto, diasRestantes);
        contexto.registrarMultaCancelacion(multa);
        contexto.cambiarEstado(new EstadoCancelada());
        contexto.getGestorEventos().notificarCancelacion(contexto);
    }

    @Override
    public String toString() {
        return "CONFIRMADA";
    }
}
