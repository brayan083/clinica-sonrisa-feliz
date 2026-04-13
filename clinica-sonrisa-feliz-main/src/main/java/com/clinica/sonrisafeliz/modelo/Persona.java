package com.clinica.sonrisafeliz.modelo;

import java.util.Objects;

/**
 * Clase base abstracta para todas las personas del sistema de la clínica.
 * Define los atributos y comportamientos comunes a Paciente, Odontologo y Recepcionista.
 */
public abstract class Persona {

    protected final Long   id;
    protected String       nombre;
    protected String       apellido;
    protected String       email;

    // ── Constructor ────────────────────────────────────────────

    /**
     * Cada subclase genera su propio ID con su propio contador y lo pasa aquí.
     */
    protected Persona(Long id, String nombre, String apellido, String email) {
        this.id       = id;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.email    = email;
    }

    // ── Método abstracto ───────────────────────────────────────

    /**
     * Devuelve el nombre completo de la persona.
     * Cada subclase puede formatear el nombre a su manera.
     */
    public abstract String getNombreCompleto();

    // ── Getters / Setters ──────────────────────────────────────
    // El ID no tiene setter: se genera en la subclase y nunca debe cambiar.

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // ── equals / hashCode ──────────────────────────────────────

    /**
     * Implementación base: igualdad por ID.
     * Las subclases pueden sobreescribir esto para usar su identidad de negocio.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona)) return false;
        Persona persona = (Persona) o;
        return Objects.equals(id, persona.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Persona{id=" + id
                + ", nombre='" + getNombreCompleto() + '\''
                + ", email='" + email + '\'' + '}';
    }
}
