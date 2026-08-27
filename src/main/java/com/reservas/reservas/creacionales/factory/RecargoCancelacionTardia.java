package com.reservas.reservas.creacionales.factory;
public final class RecargoCancelacionTardia implements PoliticaRecargo { public double calcularRecargo(double montoBase) { RecargoTemporadaAlta.validar(montoBase); return montoBase * .10; } }
