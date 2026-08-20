package com.reservas.reservas;

import java.io.FileWriter;
import java.io.IOException;

public class ArchivoReservas {
    public static void guardar(Reserva reserva) {
        try (FileWriter archivo = new FileWriter("reservas.txt", true)) { archivo.write(reserva + "\n"); }
        catch (IOException e) { throw new RuntimeException(e); }
    }
}
