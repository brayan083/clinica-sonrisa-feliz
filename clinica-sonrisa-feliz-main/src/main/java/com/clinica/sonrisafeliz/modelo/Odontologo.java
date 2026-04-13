package com.clinica.sonrisafeliz.modelo;

import java.util.Objects;

/**
 * Representa a un profesional odontológico habilitado para atender pacientes.
 * La matrícula es su dato clave de auditoría y habilitación profesional,
 * y es única en el sistema.
 */
public class Odontologo extends Persona {

    /** Contador estático para autogenerar IDs únicos y crecientes. */
    private static long contadorId = 1;

    private String matricula;

    // ── Constructor ────────────────────────────────────────────

    public Odontologo(String nombre, String apellido, String email, String matricula) {
        super(contadorId++, nombre, apellido, email);
        this.matricula = matricula;
    }

    // ── Getters / Setters ──────────────────────────────────────

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    // ── Métodos de negocio ─────────────────────────────────────

    @Override
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Dos odontólogos son iguales si tienen la misma matrícula.
     * La matrícula es la identidad de negocio del profesional.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Odontologo)) return false;
        Odontologo that = (Odontologo) o;
        return Objects.equals(matricula, that.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }

    @Override
    public String toString() {
        return "Odontologo{" +
                "id=" + id +
                ", nombre='" + getNombreCompleto() + '\'' +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}
