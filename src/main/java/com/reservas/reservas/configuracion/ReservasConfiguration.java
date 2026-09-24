package com.reservas.reservas.configuracion;

import java.nio.file.Path;
import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
import com.reservas.reservas.repository.ReservaArchivoRepository;
import com.reservas.reservas.repository.ReservaRepository;
import com.reservas.reservas.services.CalculadorTarifa;
import com.reservas.reservas.services.ProcesadorReservas;
import com.reservas.reservas.services.ServicioConfirmacionReservas;
import com.reservas.reservas.services.HotelService;
import com.reservas.reservas.mapper.HotelMapper;
import com.reservas.reservas.repository.HotelRepository;
import com.reservas.reservas.repository.HotelEnMemoriaRepository;

/** Composición de dependencias; el dominio permanece independiente de Spring. */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ReservasProperties.class)
public class ReservasConfiguration {
    @Bean
    HotelRepository hotelRepository() { return new HotelEnMemoriaRepository(); }

    @Bean
    HotelMapper hotelMapper() { return new HotelMapper(); }

    @Bean
    HotelService hotelService(HotelRepository repositorio, ServicioConfirmacionReservas confirmaciones) {
        return new HotelService(repositorio, confirmaciones);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    ProcesadorReservas procesadorReservas() {
        // Cada consumidor mantiene sus propios contadores de confirmación.
        return new ProcesadorReservas();
    }

    @Bean
    ReservaRepository reservaRepository(ReservasProperties propiedades) {
        return new ReservaArchivoRepository(Path.of(propiedades.archivo()));
    }

    @Bean
    FacturadorElectronico facturadorElectronico(ReservasProperties propiedades) {
        return new DianInvoiceAdapter(propiedades.facturacion().nit());
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
