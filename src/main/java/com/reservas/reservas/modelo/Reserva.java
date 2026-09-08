package com.reservas.reservas.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import com.reservas.reservas.creacionales.factory.PoliticaRecargo;

import com.reservas.reservas.utilidades.IdRandomizer;
import com.universidad.reservas.comportamentales.observer.GestorEventosReserva;
import com.universidad.reservas.comportamentales.state.EstadoPendiente;
import com.universidad.reservas.comportamentales.state.EstadoReserva;
import com.universidad.reservas.comportamentales.strategy.CancelacionFlexible;
import com.universidad.reservas.comportamentales.strategy.EstrategiaCancelacion;

public class Reserva {

    private int id;
    private Cliente cliente;
    private RangoFechas estadia;
    private final List<PoliticaRecargo> politicasRecargo = new ArrayList<>();
    private EstadoReserva estado;
    private EstrategiaCancelacion estrategiaCancelacion;
    private GestorEventosReserva gestorEventos;
    private double total;
    private double multaCancelacion;

    public Reserva(Cliente cliente, LocalDate fechaInicio) {
        this(IdRandomizer.generar(), cliente, fechaInicio);
    }

    public Reserva(int id, Cliente cliente, LocalDate fechaInicio) {
        this(id, cliente, fechaInicio, 0.0, new CancelacionFlexible(), new GestorEventosReserva());
    }

    public Reserva(Cliente cliente, LocalDate fechaInicio, double total,
                   EstrategiaCancelacion estrategiaCancelacion,
                   GestorEventosReserva gestorEventos) {
        this(IdRandomizer.generar(), cliente, fechaInicio, total, estrategiaCancelacion, gestorEventos);
    }

    public Reserva(int id, Cliente cliente, LocalDate fechaInicio, double total,
                   EstrategiaCancelacion estrategiaCancelacion,
                   GestorEventosReserva gestorEventos) {
        this(id, cliente, unaNoche(fechaInicio), total, estrategiaCancelacion, gestorEventos);
    }

    public Reserva(Cliente cliente, RangoFechas estadia) {
        this(IdRandomizer.generar(), cliente, estadia);
    }

    public Reserva(int id, Cliente cliente, RangoFechas estadia) {
        this(id, cliente, estadia, 0.0, new CancelacionFlexible(), new GestorEventosReserva());
    }

    public Reserva(Cliente cliente, RangoFechas estadia, double total,
                   EstrategiaCancelacion estrategiaCancelacion, GestorEventosReserva gestorEventos) {
        this(IdRandomizer.generar(), cliente, estadia, total, estrategiaCancelacion, gestorEventos);
    }

    public Reserva(int id, Cliente cliente, RangoFechas estadia, double total,
                   EstrategiaCancelacion estrategiaCancelacion, GestorEventosReserva gestorEventos) {
        this.id = id;
        this.cliente = Objects.requireNonNull(cliente, "El cliente es obligatorio");
        this.estadia = Objects.requireNonNull(estadia, "La estadía es obligatoria");
        validarMonto(total, "El total");
        this.total = total;
        this.estrategiaCancelacion = Objects.requireNonNull(
                estrategiaCancelacion, "La estrategia de cancelación es obligatoria");
        this.gestorEventos = Objects.requireNonNull(gestorEventos, "El gestor de eventos es obligatorio");
        this.estado = new EstadoPendiente();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) {
        this.cliente = Objects.requireNonNull(cliente, "El cliente es obligatorio");
    }

    private static RangoFechas unaNoche(LocalDate inicio) {
        Objects.requireNonNull(inicio, "La fecha de inicio es obligatoria");
        return new RangoFechas(inicio, inicio.plusDays(1));
    }

    public RangoFechas getEstadia() { return estadia; }
    public LocalDate getFechaInicio() { return estadia.fechaInicio(); }
    public LocalDate getFechaFin() { return estadia.fechaFin(); }

    /** Reemplaza el valor inmutable conservando la duración de la estadía. */
    public void setFechaInicio(LocalDate fechaInicio) {
        Objects.requireNonNull(fechaInicio, "La fecha de inicio es obligatoria");
        estadia = new RangoFechas(fechaInicio, fechaInicio.plusDays(estadia.duracionEnDias()));
    }

    public List<PoliticaRecargo> getPoliticasRecargo() { return List.copyOf(politicasRecargo); }

    public void agregarPoliticaRecargo(PoliticaRecargo politica) {
        politicasRecargo.add(Objects.requireNonNull(politica, "La política es obligatoria"));
    }

    /** Suma los recargos sobre la misma base, sin capitalizarlos ni modificar el total. */
    public double calcularTotalConRecargos(double montoBase) {
        validarMonto(montoBase, "El monto base");
        double resultado = montoBase;
        for (PoliticaRecargo politica : politicasRecargo) {
            double recargo = politica.calcularRecargo(montoBase);
            validarMonto(recargo, "El recargo");
            resultado += recargo;
        }
        validarMonto(resultado, "El total con recargos");
        return resultado;
    }

    public EstadoReserva getEstado() { return estado; }
    public void cambiarEstado(EstadoReserva estado) {
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
    }

    public EstrategiaCancelacion getEstrategiaCancelacion() { return estrategiaCancelacion; }
    public void setEstrategiaCancelacion(EstrategiaCancelacion estrategiaCancelacion) {
        this.estrategiaCancelacion = Objects.requireNonNull(
                estrategiaCancelacion, "La estrategia de cancelación es obligatoria");
    }

    public GestorEventosReserva getGestorEventos() { return gestorEventos; }
    public void setGestorEventos(GestorEventosReserva gestorEventos) {
        this.gestorEventos = Objects.requireNonNull(gestorEventos, "El gestor de eventos es obligatorio");
    }

    public double getTotal() { return total; }
    public void setTotal(double total) {
        validarMonto(total, "El total");
        this.total = total;
    }

    public double getMultaCancelacion() { return multaCancelacion; }
    public void registrarMultaCancelacion(double multaCancelacion) {
        validarMonto(multaCancelacion, "La multa");
        this.multaCancelacion = multaCancelacion;
    }

    private static void validarMonto(double monto, String nombre) {
        if (!Double.isFinite(monto) || monto < 0) {
            throw new IllegalArgumentException(nombre + " debe ser un número finito y no negativo");
        }
    }

    public void confirmar() { estado.confirmar(this); }

    public void cancelar(int diasRestantes) { estado.cancelar(this, diasRestantes); }

    /** Atajo que calcula los días restantes respecto de la fecha actual. */
    public void cancelar() {
        int diasRestantes = (int) Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(), getFechaInicio()));
        cancelar(diasRestantes);
    }

    /** Punto polimórfico utilizado por el observador de habitaciones. */
    public void liberarHabitacion() {
        // Una reserva básica no tiene una habitación asociada.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        Reserva reserva = (Reserva) o;
        return id == reserva.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Reserva{id=" + id + ", cliente=" + cliente.getNombre()
                + ", estadia=" + estadia + ", estado=" + estado
                + ", total=" + total + ", multaCancelacion=" + multaCancelacion + "}";
    }
}
