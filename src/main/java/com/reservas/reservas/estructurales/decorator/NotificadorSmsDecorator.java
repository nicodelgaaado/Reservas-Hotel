package com.reservas.reservas.estructurales.decorator;
import java.util.Objects;
import com.reservas.reservas.modelo.Cliente;
import com.reservas.reservas.notificaciones.CanalNotificacion;
public final class NotificadorSmsDecorator implements CanalNotificacion {
    private final CanalNotificacion base;
    public NotificadorSmsDecorator(CanalNotificacion base) { this.base=Objects.requireNonNull(base); }
    public void notificar(Cliente cliente, String mensaje) { base.notificar(cliente, mensaje); System.out.println("[SMS " + cliente.getTelefono() + "] " + mensaje); }
}
