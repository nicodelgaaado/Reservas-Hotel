package com.reservas.reservas.estructurales.adapter;
import com.reservas.reservas.modelo.Reserva;
public interface FacturadorElectronico { String emitirFactura(Reserva reserva, double total); }
