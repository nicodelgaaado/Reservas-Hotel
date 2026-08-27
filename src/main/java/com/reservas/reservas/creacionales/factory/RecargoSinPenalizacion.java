package com.reservas.reservas.creacionales.factory;
public final class RecargoSinPenalizacion implements PoliticaRecargo { public double calcularRecargo(double montoBase) { RecargoTemporadaAlta.validar(montoBase); return 0; } }
