package com.clinica.sonrisafeliz.modelo;

import com.clinica.sonrisafeliz.enums.EstadoTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private List<Turno> turnos;

    public Agenda() {
        this.turnos = new ArrayList<>();
    }

    public void agregarTurno(Turno turno){
        this.turnos.add(turno);
    }

    public boolean estaDisponible(LocalDate fecha, LocalTime hora) {
        for (Turno t : turnos) {
            if (t.getFecha().equals(fecha) && t.getHora().equals(hora) && t.getEstado() != EstadoTurno.CANCELADO) {
                return false;
            }
        }
        return true;
    }

    public List<Turno> getTurnos() { return turnos; }
    public void setTurnos(List<Turno> turnos) { this.turnos = turnos; }
}
