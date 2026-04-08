package com.clinica.sonrisafeliz.modelo;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa a un paciente de la clínica odontológica.
 * Tiene asociado un Domicilio en relación de composición:
 * el domicilio no existe independientemente del paciente.
 *
 * La identidad del paciente se define por su DNI (único en el sistema).
 */
public class Paciente extends Persona {

    /** Contador estático para autogenerar IDs únicos y crecientes. */
    private static long contadorId = 1;

    private String    dni;
    private LocalDate fechaIngreso;
    private Domicilio domicilio;

    // ── Constructores ──────────────────────────────────────────

    /**
     * Constructor principal. El ID se genera automáticamente.
     * La fecha de ingreso se asigna al día de hoy (fecha de alta en el sistema).
     */
    public Paciente(String nombre, String apellido, String dni,
                    String email, Domicilio domicilio) {
        super(contadorId++, nombre, apellido, email);
        this.dni          = dni;
        this.domicilio    = domicilio;
        this.fechaIngreso = LocalDate.now();
    }

    /**
     * Constructor secundario. El ID se genera automáticamente.
     * Permite especificar la fecha de ingreso manualmente.
     */
    public Paciente(String nombre, String apellido, String dni,
                    String email, LocalDate fechaIngreso, Domicilio domicilio) {
        super(contadorId++, nombre, apellido, email);
        this.dni          = dni;
        this.fechaIngreso = fechaIngreso;
        this.domicilio    = domicilio;
    }

    // ── Getters / Setters ──────────────────────────────────────

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public Domicilio getDomicilio() { return domicilio; }
    public void setDomicilio(Domicilio domicilio) { this.domicilio = domicilio; }

    // ── Métodos de negocio ─────────────────────────────────────

    @Override
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Dos pacientes son iguales si tienen el mismo DNI.
     * El DNI es la identidad de negocio del paciente en este sistema.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(dni, paciente.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", nombre='" + getNombreCompleto() + '\'' +
                ", dni='" + dni + '\'' +
                ", email='" + email + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", domicilio=" + domicilio +
                '}';
    }
}
