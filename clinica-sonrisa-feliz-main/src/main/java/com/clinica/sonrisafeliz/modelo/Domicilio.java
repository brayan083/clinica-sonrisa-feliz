package com.clinica.sonrisafeliz.modelo;

/**
 * Representa el domicilio de un paciente.
 * Es una clase de soporte que no tiene gestión autónoma:
 * existe únicamente en el contexto de un Paciente (composición).
 */
public class Domicilio {

    /** Contador estático para autogenerar IDs únicos y crecientes. */
    private static long contadorId = 1;

    private final Long id;
    private String calle;
    private String numero;
    private String localidad;
    private String provincia;

    // ── Constructores ──────────────────────────────────────────

    /**
     * Constructor principal. El ID se genera automáticamente.
     */
    public Domicilio(String calle, String numero, String localidad, String provincia) {
        this.id        = contadorId++;
        this.calle     = calle;
        this.numero    = numero;
        this.localidad = localidad;
        this.provincia = provincia;
    }

    public Domicilio(){
        this.id = contadorId++;
    }

    // ── Getters ────────────────────────────────────────────────
    // El ID no tiene setter: se genera automáticamente y nunca debe cambiar.

    public Long getId() { return id; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    // ── Métodos de negocio ─────────────────────────────────────

    /**
     * Devuelve una representación legible del domicilio.
     * Ejemplo: "Av. Corrientes 1234, Buenos Aires, Buenos Aires"
     */
    @Override
    public String toString() {
        return calle + " " + numero + ", " + localidad + ", " + provincia;
    }
}
