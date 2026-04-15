package com.clinica.sonrisafeliz.modelo;

import com.clinica.sonrisafeliz.enums.EstadoTurno;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Recepcionista extends Persona{
    private String legajo;

    public Recepcionista(String nombre, String apellido, String email, String legajo) {
        super(nombre, apellido, email);
        this.legajo = legajo;
    }

    private boolean esFechaValida(LocalDate fecha, LocalTime hora) {
        LocalDateTime momentoSolicitado = LocalDateTime.of(fecha, hora);
        LocalDateTime ahora = LocalDateTime.now();
        return momentoSolicitado.isAfter(ahora);
    }

    private boolean validarDisponibilidad(Odontologo odontologo, LocalDate fecha, LocalTime hora) {
        return odontologo.getAgenda().estaDisponible(fecha, hora);
    }

    public Paciente registrarPaciente(String nombre, String apellido, String email, String dni, Domicilio domicilio) {
        System.out.println("Recepcionista " + this.getNombre() + ": Registrando paciente " + nombre + "...");
        return new Paciente(nombre, apellido, email, dni, domicilio);
    }

    public Turno asignarTurno(Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora) {
        System.out.println("Recepcionista " + this.getNombre() + ": Intentando asignar turno para " + paciente.getNombreCompleto() + "...");
        if (!esFechaValida(fecha, hora)) {
            System.out.println("-> ERROR: No se puede asignar un turno en una fecha u hora del pasado.");
            return null;
        }
        if (validarDisponibilidad(odontologo, fecha, hora)) {
            Turno nuevoTurno = new Turno(paciente, odontologo, fecha, hora);
            odontologo.getAgenda().agregarTurno(nuevoTurno);
            paciente.agregarTurno(nuevoTurno);
            System.out.println("-> ¡Turno asignado con éxito!");
            return nuevoTurno;
        } else {
            System.out.println("-> ERROR: El Odontólogo " + odontologo.getNombreCompleto() + " no está disponible en ese horario.");
            return null;
        }
    }

    public void modificarTurno(Turno turno, LocalDate nuevaFecha, LocalTime nuevaHora) {
        System.out.println("Recepcionista " + this.getNombre() + ": Modificando fecha del turno...");
        if (!esFechaValida(nuevaFecha, nuevaHora)) {
            System.out.println("-> ERROR: No se puede reprogramar un turno para una fecha u hora pasada.");
            return;
        }
        if (validarDisponibilidad(turno.getOdontologo(), nuevaFecha, nuevaHora)) {
            turno.setFecha(nuevaFecha);
            turno.setHora(nuevaHora);
            turno.setEstado(EstadoTurno.PENDIENTE);
            System.out.println("-> ¡Turno modificado con éxito!");
        } else {
            System.out.println("-> ERROR: El Odontólogo " + turno.getOdontologo().getNombreCompleto() + " no está disponible en ese nuevo horario.");
        }
    }

    public void cancelarTurno(Turno turno) {
        System.out.println("Recepcionista " + this.getNombre() + ": Cancelando turno de " + turno.getPaciente().getNombreCompleto());
        turno.setEstado(EstadoTurno.CANCELADO);
    }

    public String getLegajo() { return legajo; }
    public void setLegajo(String legajo) { this.legajo = legajo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recepcionista that = (Recepcionista) o;
        return Objects.equals(legajo, that.legajo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(legajo);
    }

    @Override
    public String toString() {
        return "Recepcionista{" +
                "nombre='" + getNombreCompleto() + '\'' +
                ", legajo='" + legajo + '\'' +
                '}';
    }
}
