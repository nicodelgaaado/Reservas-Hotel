package com.reservas.reservas.estructurales.decorator;
import java.time.Instant;
import java.util.Objects;
import com.reservas.reservas.modelo.Cliente;
import com.reservas.reservas.notificaciones.CanalNotificacion;
public final class NotificadorAuditoriaDecorator implements CanalNotificacion {
    private final CanalNotificacion base;
    public NotificadorAuditoriaDecorator(CanalNotificacion base) { this.base=Objects.requireNonNull(base); }
    public void notificar(Cliente cliente, String mensaje) { System.out.println("[AUDITORIA UTC " + Instant.now() + "] cliente=" + cliente.getId()); base.notificar(cliente, mensaje); }
}
