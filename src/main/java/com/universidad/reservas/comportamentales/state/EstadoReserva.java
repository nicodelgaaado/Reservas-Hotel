package com.universidad.reservas.comportamentales.state;

import com.reservas.reservas.modelo.Reserva;

/** Estado polimórfico que controla el ciclo de vida de una reserva. */
public interface EstadoReserva {
    void confirmar(Reserva contexto);

    void cancelar(Reserva contexto, int diasRestantes);
}
