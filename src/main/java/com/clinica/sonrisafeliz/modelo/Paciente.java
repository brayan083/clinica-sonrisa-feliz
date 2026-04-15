package com.clinica.sonrisafeliz.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Paciente extends Persona{
    private String dni;
    private LocalDate fechaIngreso;
    private Domicilio domicilio;
    private List<Turno> turnos;

    public Paciente(String nombre, String apellido, String email, String dni, Domicilio domicilio) {
        super(nombre, apellido, email);
        this.dni = dni;
        this.domicilio = domicilio;
        this.fechaIngreso = LocalDate.now();
        this.turnos = new ArrayList<>();
    }

    public void agregarTurno(Turno turno) {
        this.turnos.add(turno);
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public Domicilio getDomicilio() { return domicilio; }
    public void setDomicilio(Domicilio domicilio) { this.domicilio = domicilio; }
    public List<Turno> getTurnos() { return turnos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
                "nombreCompleto='" + getNombreCompleto() + '\'' +
                ", dni='" + dni + '\'' +
                ", domicilio=" + (domicilio != null ? domicilio.toString() : "Sin Domicilio") +
                '}';
    }
}
