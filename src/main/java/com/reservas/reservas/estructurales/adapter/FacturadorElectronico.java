package com.reservas.reservas.estructurales.adapter;
import com.reservas.reservas.domain.Reserva;
public interface FacturadorElectronico { String emitirFactura(Reserva reserva, double total); }
