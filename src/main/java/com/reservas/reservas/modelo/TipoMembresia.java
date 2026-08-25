package com.reservas.reservas.modelo;

public enum TipoMembresia {
    VIP(0.20),
    CORPORATIVO(0.30),
    REGULAR(0.00);

    private final double porcentajeDescuento;

    TipoMembresia(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
}
