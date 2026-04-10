package com.clinica.sonrisafeliz.modelo;

public class Domicilio {
    private Long id;
    private String calle;
    private Integer numero;
    private String localidad;
    private String provincia;

    public Domicilio() {}

    public Domicilio(String calle, int numero, String localidad, String provincia) {
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.provincia = provincia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    @Override
    public String toString() {
        return calle + " " + numero + ", " + localidad + ", " + provincia;
    }
}
