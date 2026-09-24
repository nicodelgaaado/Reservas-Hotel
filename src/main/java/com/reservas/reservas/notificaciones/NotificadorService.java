package com.reservas.reservas.notificaciones;
import com.reservas.reservas.domain.Cliente;
@FunctionalInterface
public interface NotificadorService { void notificar(Cliente cliente, String mensaje); }
