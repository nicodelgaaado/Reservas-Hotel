package com.reservas.reservas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Cliente {

    private static final int LIMITE_PENALIZACIONES = 3;

    private int id;
    private String nombre;
    private String documentoIdentidad;
    private String email;
    private String telefono;
    private LocalDate fechaRegistro;
    private int penalizaciones;
    private final List<String> historialPenalizaciones;
    private TipoMembresia membresia;

    public Cliente(int id, String nombre, String documentoIdentidad, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.documentoIdentidad = documentoIdentidad;
        this.email = email;
        this.telefono = telefono;
        this.fechaRegistro = LocalDate.now();
        this.penalizaciones = 0;
        this.historialPenalizaciones = new ArrayList<>();
        this.membresia = TipoMembresia.REGULAR;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }

    public TipoMembresia getMembresia() { return membresia; }

    public void setMembresia(TipoMembresia membresia) {
        if (membresia == null) {
            throw new IllegalArgumentException("La membresía no puede ser nula");
        }
        this.membresia = membresia;
    }

    public int getPenalizaciones() { return penalizaciones; }

    /**
     * Registra una penalización asociada al cliente.
     *
     * @param motivo razón de la penalización
     */
    public void agregarPenalizacion(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("El motivo de la penalización no puede estar vacío");
        }

        penalizaciones++;
        historialPenalizaciones.add(motivo.trim());
    }

    /** Reduce en uno el número de penalizaciones, sin permitir valores negativos. */
    public void quitarPenalizacion() {
        if (penalizaciones > 0) {
            penalizaciones--;
            if (!historialPenalizaciones.isEmpty()) {
                historialPenalizaciones.remove(historialPenalizaciones.size() - 1);
            }
        }
    }

    public List<String> getHistorialPenalizaciones() {
        return Collections.unmodifiableList(historialPenalizaciones);
    }

    public boolean estaBloqueado() {
        return penalizaciones >= LIMITE_PENALIZACIONES;
    }

    public void limpiarPenalizaciones() {
        penalizaciones = 0;
        historialPenalizaciones.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return id == cliente.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nombre='" + nombre + "', email='" + email
                + "', penalizaciones=" + penalizaciones + ", bloqueado=" + estaBloqueado() + "}";
    }
}
