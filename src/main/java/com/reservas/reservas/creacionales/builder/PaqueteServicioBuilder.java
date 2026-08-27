package com.reservas.reservas.creacionales.builder;

import com.reservas.reservas.modelo.PaqueteServicio;

public final class PaqueteServicioBuilder {
    private boolean desayuno, spa, transporte;
    private int personas;
    public PaqueteServicioBuilder incluyeDesayuno() { desayuno = true; return this; }
    public PaqueteServicioBuilder incluyeSpa() { spa = true; return this; }
    public PaqueteServicioBuilder incluyeTransporteAeropuerto() { transporte = true; return this; }
    public PaqueteServicioBuilder numeroPersonas(int personas) { this.personas = personas; return this; }
    public PaqueteServicio build() {
        if (personas < 1) throw new IllegalStateException("El número de personas debe ser al menos 1");
        if (transporte && personas > 4) throw new IllegalStateException("El transporte admite máximo 4 personas");
        return new PaqueteServicio(desayuno, spa, transporte, personas);
    }
}
