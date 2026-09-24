package com.reservas.reservas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.reservas.reservas.domain.*;
import com.reservas.reservas.creacionales.factory.*;
import com.reservas.reservas.services.*;
import com.universidad.reservas.comportamentales.state.EstadoCancelada;

/** Pruebas del modelo ejecutables con -ea, sin dependencias externas. */
public final class PruebaModeloHotel {
    public static void main(String[] args) {
        Cliente cliente = new Cliente(1, "Ana", "DOC-1", "ana@example.com", "3001234567");
        HabitacionEstandar estandar = new HabitacionEstandar(new NumeroHabitacion(101), new CapacidadMaxima(2), 2);
        assert estandar.getNumeroCamas() == 2;
        List<String> amenidades = new ArrayList<>(List.of("Jacuzzi"));
        SuitePresidencial suite = new SuitePresidencial(new NumeroHabitacion(501), new CapacidadMaxima(4), amenidades, true);
        amenidades.clear();
        assert suite.tieneServicioMayordomo();
        assert suite.getAmenidadesLujo().equals(List.of("Jacuzzi"));
        falla(UnsupportedOperationException.class, () -> suite.getAmenidadesLujo().clear());
        falla(IllegalArgumentException.class, () -> new HabitacionEstandar(new NumeroHabitacion(102), new CapacidadMaxima(2), 0));
        falla(IllegalArgumentException.class, () -> new SuitePresidencial(new NumeroHabitacion(502), new CapacidadMaxima(2), List.of(" "), true));

        LocalDate inicio = LocalDate.of(2026, 10, 10);
        RangoFechas rango = new RangoFechas(inicio, inicio.plusDays(3));
        assert rango.equals(new RangoFechas(inicio, inicio.plusDays(3)));
        falla(IllegalArgumentException.class, () -> new RangoFechas(inicio, inicio));
        falla(IllegalArgumentException.class, () -> new RangoFechas(inicio, inicio.minusDays(1)));
        falla(NullPointerException.class, () -> new RangoFechas(null, inicio));
        ReservaHabitacion reserva = new ReservaHabitacion(10, cliente, estandar, rango);
        assert estandar.getHistorialReservas().equals(List.of(reserva));
        assert suite.getHistorialReservas().isEmpty();
        falla(UnsupportedOperationException.class, () -> estandar.getHistorialReservas().clear());
        reserva.setFechaInicio(inicio.plusDays(5));
        assert reserva.getFechaInicio().equals(reserva.getEstadia().fechaInicio());
        assert reserva.getFechaFin().equals(inicio.plusDays(8));
        assert rango.fechaInicio().equals(inicio);
        assert new Reserva(cliente, inicio).getEstadia().duracionEnDias() == 1;

        reserva.agregarPoliticaRecargo(new RecargoTemporadaAlta());
        reserva.agregarPoliticaRecargo(new RecargoCancelacionTardia());
        reserva.agregarPoliticaRecargo(base -> 50);
        assert reserva.calcularTotalConRecargos(1000) == 1350;
        assert reserva.calcularTotalConRecargos(1000) == 1350;
        assert new CalculadorTarifa(List.of((r, base) -> base / 2)).calcular(reserva, 2000) == 1350;
        falla(UnsupportedOperationException.class, () -> reserva.getPoliticasRecargo().clear());
        falla(NullPointerException.class, () -> reserva.agregarPoliticaRecargo(null));
        falla(IllegalArgumentException.class, () -> reserva.calcularTotalConRecargos(Double.NaN));
        double esperado = reserva.calcularTotalConRecargos(1000 * (1 - cliente.getMembresia().getPorcentajeDescuento()));
        assert new ProcesadorReservas().confirmarReserva(reserva, 1000) == esperado;
        assert reserva.getTotal() == esperado;
        reserva.cancelar(5);
        assert reserva.getEstado() instanceof EstadoCancelada;
        assert estandar.getHistorialReservas().equals(List.of(reserva));
        ReservaHabitacion segunda = new ReservaHabitacion(11, cliente, estandar, rango);
        assert estandar.getHistorialReservas().equals(List.of(reserva, segunda));
        Reserva invalida = new Reserva(cliente, rango);
        invalida.agregarPoliticaRecargo(base -> -1);
        falla(IllegalArgumentException.class, () -> invalida.calcularTotalConRecargos(100));
        System.out.println("Todas las pruebas del modelo hotelero pasaron.");
    }

    private static void falla(Class<? extends RuntimeException> tipo, Runnable accion) {
        try {
            accion.run();
        } catch (RuntimeException error) {
            if (tipo.isInstance(error)) return;
            throw error;
        }
        throw new AssertionError("Se esperaba " + tipo.getSimpleName());
    }
}
