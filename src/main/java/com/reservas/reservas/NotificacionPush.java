package com.reservas.reservas;

import java.util.Objects;

public final class NotificacionPush implements CanalNotificacion {
    @Override
    public void notificar(Cliente cliente, String mensaje) {
        Objects.requireNonNull(cliente, "El cliente es obligatorio");
        System.out.println("[PUSH APP - " + cliente.getNombre() + "] " + mensaje);
    }
}
