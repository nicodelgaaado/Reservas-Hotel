package com.reservas.reservas.notificaciones;

import java.util.Objects;

import com.reservas.reservas.modelo.Cliente;

public final class NotificacionPush implements CanalNotificacion {
    @Override
    public void notificar(Cliente cliente, String mensaje) {
        Objects.requireNonNull(cliente, "El cliente es obligatorio");
        System.out.println("[PUSH APP - " + cliente.getNombre() + "] " + mensaje);
    }
}
