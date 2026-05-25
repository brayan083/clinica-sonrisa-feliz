package clinicasonrisafeliz.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import clinicasonrisafeliz.enums.EstadoTurno;

public class Turno implements Comparable<Turno> {
    private static long contadorId = 1;

    private final Long id;
    private Paciente paciente;
    private Odontologo odontologo;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoTurno estado;
    private Recepcionista recepcionista;

    public Turno(Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, Recepcionista recepcionista) {
        this.id = contadorId++;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = EstadoTurno.PENDIENTE;
        this.recepcionista = recepcionista;
    }

    /** Constructor usado al cargar desde CSV; ajusta el contador para evitar colisiones. */
    public Turno(long id, Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, EstadoTurno estado, Recepcionista recepcionista) {
        this.id = id;
        if (id >= contadorId) contadorId = id + 1;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.recepcionista = recepcionista;
    }

    @Override
    public int compareTo(Turno otro) {
        int cmp = this.fecha.compareTo(otro.fecha);
        if (cmp != 0) return cmp;
        return this.hora.compareTo(otro.hora);
    }

    public boolean esFuturo() {
        LocalDateTime fechaHoraTurno = LocalDateTime.of(this.fecha, this.hora);
        return fechaHoraTurno.isAfter(LocalDateTime.now());
    }

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
    public Recepcionista getRecepcionista() { return recepcionista; }

    @Override
    public String toString() {
        return "Turno{" +
                "fecha=" + fecha +
                ", hora=" + hora +
                ", estado=" + estado +
                ", paciente=" + paciente.getNombreCompleto() +
                ", odontologo=" + odontologo.getNombreCompleto() +
                ", recepcionista=" + recepcionista.getNombreCompleto() +
                '}';
    }
}
