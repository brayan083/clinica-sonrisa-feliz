package com.clinica.sonrisafeliz.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa a una recepcionista de la clínica odontológica.
 * Es la responsable de asignar turnos entre pacientes y odontólogos.
 */
public class Recepcionista extends Persona {

    /** Contador estático para autogenerar IDs únicos y crecientes. */
    private static long contadorId = 1;

    private String legajo;

    // ── Constructor ────────────────────────────────────────────

    public Recepcionista(String nombre, String apellido, String email, String legajo) {
        super(contadorId++, nombre, apellido, email);
        this.legajo = legajo;
    }

    // ── Getters / Setters ──────────────────────────────────────

    public String getLegajo() { return legajo; }
    public void setLegajo(String legajo) { this.legajo = legajo; }

    // ── Métodos de negocio ─────────────────────────────────────

    /**
     * Crea y devuelve un nuevo Turno entre el paciente y el odontólogo indicados.
     * El turno se genera en estado PENDIENTE.
     */
    public Turno asignarTurno(Paciente p, Odontologo o, LocalDate fecha, LocalTime hora) {
        return new Turno(p, o, fecha, hora);
    }

    @Override
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return "Recepcionista{" +
                "id=" + id +
                ", nombre='" + getNombreCompleto() + '\'' +
                ", legajo='" + legajo + '\'' +
                '}';
    }
}
