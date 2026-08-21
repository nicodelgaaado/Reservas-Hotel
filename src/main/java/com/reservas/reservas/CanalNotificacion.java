package com.reservas.reservas;

@FunctionalInterface
public interface CanalNotificacion {
    void notificar(Cliente cliente, String mensaje);
}
