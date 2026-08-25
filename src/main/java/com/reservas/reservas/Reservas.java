package com.reservas.reservas;

import java.time.LocalDate;

import com.reservas.reservas.modelo.Cliente;
import com.reservas.reservas.modelo.Reserva;
import com.reservas.reservas.persistencia.ArchivoReservas;
import com.reservas.reservas.servicios.ProcesadorReservas;

/**
 * @author Nicolas
 */
public class Reservas {

    public static void main(String[] args) {
        Cliente cliente1 = new Cliente(1, "Nicolas Delgado", "1085123456", "nicolas@example.com", "3001234567");
        Reserva reserva1 = new Reserva(cliente1, LocalDate.of(2026, 8, 20));
        ProcesadorReservas procesador = new ProcesadorReservas();

        System.out.println(reserva1);
        double tarifaFinal = procesador.confirmarReserva(reserva1, 100000);
        System.out.println("Despues de confirmar: " + reserva1);
        System.out.println("Tarifa final: " + tarifaFinal);
        ArchivoReservas.guardar(reserva1);
    }
}
