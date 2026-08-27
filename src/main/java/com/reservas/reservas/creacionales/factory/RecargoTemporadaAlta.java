package com.reservas.reservas.creacionales.factory;
public final class RecargoTemporadaAlta implements PoliticaRecargo { public double calcularRecargo(double montoBase) { validar(montoBase); return montoBase * .20; } static void validar(double m) { if (m < 0) throw new IllegalArgumentException("El monto no puede ser negativo"); } }
