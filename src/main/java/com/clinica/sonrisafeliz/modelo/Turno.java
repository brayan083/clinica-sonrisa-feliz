package com.clinica.sonrisafeliz.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.clinica.sonrisafeliz.modelo.Enums.EstadoTurno;

/**
 * Representa un turno en la clínica odontológica.
 * Vincula un Paciente con un Odontólogo en una fecha y hora determinadas.
 * Su ciclo de vida está modelado por el enum EstadoTurno.
 */
public class Turno {

    /** Contador estático para autogenerar IDs únicos y crecientes. */
    private static long contadorId = 1;

    private final Long id;
    private Paciente   paciente;
    private Odontologo odontologo;
    private LocalDate  fecha;
    private LocalTime  hora;
    private EstadoTurno estado;

    // ── Constructores ──────────────────────────────────────────

    /**
     * Constructor principal. El ID se genera automáticamente.
     * El turno se crea siempre en estado PENDIENTE.
     */
    public Turno(Paciente paciente, Odontologo odontologo,
                 LocalDate fecha, LocalTime hora) {
        this.id         = contadorId++;
        this.paciente   = paciente;
        this.odontologo = odontologo;
        this.fecha      = fecha;
        this.hora       = hora;
        this.estado     = EstadoTurno.PENDIENTE;
    }

    // ── Getters ────────────────────────────────────────────────
    // El ID no tiene setter: se genera automáticamente y nunca debe cambiar.

    public Long getId() { return id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Odontologo getOdontologo() { return odontologo; }
    public void setOdontologo(Odontologo odontologo) { this.odontologo = odontologo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public EstadoTurno getEstado() { return estado; }
    public void setEstado(EstadoTurno estado) { this.estado = estado; }

    // ── Métodos de negocio ─────────────────────────────────────

    /**
     * Indica si el turno es en el futuro (fecha posterior a hoy).
     */
    public boolean esFuturo() {
        return fecha != null && fecha.isAfter(LocalDate.now());
    }

    /**
     * Indica si el turno está disponible para modificaciones:
     * solo los turnos PENDIENTES o CONFIRMADOS que son futuros.
     */
    public boolean estaDisponible() {
        return esFuturo()
                && (estado == EstadoTurno.PENDIENTE
                    || estado == EstadoTurno.CONFIRMADO);
    }

    @Override
    public String toString() {
        return "Turno{" +
                "id=" + id +
                ", paciente=" + (paciente != null ? paciente.getNombreCompleto() : "null") +
                ", odontologo=" + (odontologo != null ? odontologo.getNombreCompleto() : "null") +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", estado=" + estado +
                '}';
    }
}
