package com.reservas.reservas.notificaciones;

import com.reservas.reservas.modelo.Cliente;

@FunctionalInterface
public interface CanalNotificacion {
    void notificar(Cliente cliente, String mensaje);
}
