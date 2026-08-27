package com.reservas.reservas.modelo;

public final class PaqueteServicio {
    private final boolean incluyeDesayuno;
    private final boolean incluyeSpa;
    private final boolean incluyeTransporteAeropuerto;
    private final int numeroPersonas;

    public PaqueteServicio(boolean incluyeDesayuno, boolean incluyeSpa,
                           boolean incluyeTransporteAeropuerto, int numeroPersonas) {
        this.incluyeDesayuno = incluyeDesayuno;
        this.incluyeSpa = incluyeSpa;
        this.incluyeTransporteAeropuerto = incluyeTransporteAeropuerto;
        this.numeroPersonas = numeroPersonas;
    }
    public boolean isIncluyeDesayuno() { return incluyeDesayuno; }
    public boolean isIncluyeSpa() { return incluyeSpa; }
    public boolean isIncluyeTransporteAeropuerto() { return incluyeTransporteAeropuerto; }
    public int getNumeroPersonas() { return numeroPersonas; }
    @Override public String toString() { return "PaqueteServicio{desayuno=" + incluyeDesayuno + ", spa=" + incluyeSpa + ", transporte=" + incluyeTransporteAeropuerto + ", personas=" + numeroPersonas + "}"; }
}
