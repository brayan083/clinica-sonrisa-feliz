package com.clinica.sonrisafeliz.modelo;

import java.util.Objects;

/**
 * Representa a un profesional odontológico habilitado para atender pacientes.
 * La matrícula es su dato clave de auditoría y habilitación profesional,
 * y es única en el sistema.
 */
public class Odontologo {

    /** Contador estático para autogenerar IDs únicos y crecientes. */
    private static long contadorId = 1;

    private final Long id;
    private String nombre;
    private String apellido;
    private String matricula;

    // ── Constructores ──────────────────────────────────────────

    /**
     * Constructor principal. El ID se genera automáticamente.
     */
    public Odontologo(String nombre, String apellido, String matricula) {
        this.id        = contadorId++;
        this.nombre    = nombre;
        this.apellido  = apellido;
        this.matricula = matricula;
    }

    // ── Getters ────────────────────────────────────────────────
    // El ID no tiene setter: se genera automáticamente y nunca debe cambiar.

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    // ── Métodos de negocio ─────────────────────────────────────

    /**
     * Devuelve el nombre completo: "nombre apellido".
     */
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
