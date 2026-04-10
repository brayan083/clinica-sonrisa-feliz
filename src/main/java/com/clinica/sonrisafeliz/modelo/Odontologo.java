package com.clinica.sonrisafeliz.modelo;

import java.util.Objects;

public class Odontologo extends Persona{
    private String matricula;
    private Agenda agenda;
    
    public Odontologo() {
        super();
        this.agenda = new Agenda();
    }
    
    public Odontologo(String nombre, String apellido, String email, String matricula) {
        super(nombre, apellido, email);
        this.matricula = matricula;
        this.agenda = new Agenda();
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public Agenda getAgenda() { return agenda; }
    public void setAgenda(Agenda agenda) { this.agenda = agenda; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Odontologo that = (Odontologo) o;
        return Objects.equals(matricula, that.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }

    @Override
    public String toString() {
        return "Odontólogo{" +
                "Dr/Dra.='" + getNombreCompleto() + '\'' +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}
