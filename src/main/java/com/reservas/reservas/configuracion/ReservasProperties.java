package com.reservas.reservas.configuracion;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuracion de la aplicacion de reservas.
 * @param archivo Ruta del archivo de confirmaciones de reservas.
 * @param demo Configuracion de la demostracion al iniciar.
 * @param facturacion Datos de facturacion del hotel.
 */
@ConfigurationProperties("reservas")
public record ReservasProperties(
        @DefaultValue("reservas.txt") String archivo,
        @DefaultValue Demo demo,
        @DefaultValue Facturacion facturacion) {

    /** @param enabled Ejecuta la demostracion al iniciar la aplicacion. */
    public record Demo(@DefaultValue("true") boolean enabled) { }

    /** @param nit NIT del hotel utilizado para emitir facturas. */
    public record Facturacion(@DefaultValue("900123456-7") String nit) { }
}
