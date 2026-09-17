package com.reservas.reservas.configuracion;

import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import com.reservas.reservas.estructurales.adapter.DianInvoiceAdapter;
import com.reservas.reservas.estructurales.adapter.FacturadorElectronico;
import com.reservas.reservas.estructurales.decorator.NotificadorAuditoriaDecorator;
import com.reservas.reservas.estructurales.decorator.NotificadorSmsDecorator;
import com.reservas.reservas.notificaciones.CanalNotificacion;
import com.reservas.reservas.notificaciones.NotificacionWhatsApp;
import com.reservas.reservas.persistencia.ReservaArchivoRepository;
import com.reservas.reservas.persistencia.ReservaRepository;
import com.reservas.reservas.servicios.CalculadorTarifa;
import com.reservas.reservas.servicios.ProcesadorReservas;
import com.reservas.reservas.servicios.ServicioConfirmacionReservas;

/** Composición de dependencias; el dominio permanece independiente de Spring. */
@Configuration(proxyBeanMethods = false)
public class ReservasConfiguration {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    ProcesadorReservas procesadorReservas() {
        // Cada consumidor mantiene sus propios contadores de confirmación.
        return new ProcesadorReservas();
    }

    @Bean
    ReservaRepository reservaRepository(@Value("${reservas.archivo:reservas.txt}") String archivo) {
        return new ReservaArchivoRepository(Path.of(archivo));
    }

    @Bean
    FacturadorElectronico facturadorElectronico(@Value("${reservas.facturacion.nit:900123456-7}") String nit) {
        return new DianInvoiceAdapter(nit);
    }

    @Bean
    CanalNotificacion canalNotificacion() {
        return new NotificadorSmsDecorator(new NotificadorAuditoriaDecorator(new NotificacionWhatsApp()));
    }

    @Bean
    CalculadorTarifa calculadorTarifa() {
        return new CalculadorTarifa(List.of((reserva, tarifa) ->
                tarifa * (1 - reserva.getCliente().getMembresia().getPorcentajeDescuento())));
    }

    @Bean
    ServicioConfirmacionReservas servicioConfirmacionReservas(CalculadorTarifa calculador,
            ReservaRepository repositorio, List<CanalNotificacion> canales) {
        return new ServicioConfirmacionReservas(calculador, repositorio, canales);
    }
}
