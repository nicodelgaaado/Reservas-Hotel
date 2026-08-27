package com.reservas.reservas.creacionales.factory;
public final class PoliticaRecargoFactory { private PoliticaRecargoFactory() {} public static PoliticaRecargo crear(TipoRecargo tipo) { return switch (tipo) { case TEMPORADA_ALTA -> new RecargoTemporadaAlta(); case CANCELACION_TARDIA -> new RecargoCancelacionTardia(); case SIN_PENALIZACION -> new RecargoSinPenalizacion(); }; } }
