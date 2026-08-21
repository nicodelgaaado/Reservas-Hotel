package com.reservas.reservas;

import java.util.Objects;

public final class NotificacionWhatsApp implements CanalNotificacion {
    @Override
    public void notificar(Cliente cliente, String mensaje) {
        Objects.requireNonNull(cliente, "El cliente es obligatorio");
        String telefono = cliente.getTelefono().replaceAll("[^0-9]", "");
        if (telefono.startsWith("57")) {
            telefono = telefono.substring(2);
        }
        System.out.println("[WHATSAPP +57" + telefono + "] " + mensaje);
    }
}
