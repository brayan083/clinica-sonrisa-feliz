package com.clinica.sonrisafeliz.servicio;

import com.clinica.sonrisafeliz.enums.EstadoTurno;
import com.clinica.sonrisafeliz.excepcion.TurnoNoEncontradoException;
import com.clinica.sonrisafeliz.excepcion.TurnoYaReservadoException;
import com.clinica.sonrisafeliz.modelo.Odontologo;
import com.clinica.sonrisafeliz.modelo.Paciente;
import com.clinica.sonrisafeliz.modelo.Turno;
import com.clinica.sonrisafeliz.repositorio.RepositorioTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioTurno {

    private final RepositorioTurno repositorioTurno;
    private final ServicioPaciente servicioPaciente;
    private final ServicioOdontologo servicioOdontologo;

    public ServicioTurno(RepositorioTurno repositorioTurno,
                         ServicioPaciente servicioPaciente,
                         ServicioOdontologo servicioOdontologo) {
        this.repositorioTurno = repositorioTurno;
        this.servicioPaciente = servicioPaciente;
        this.servicioOdontologo = servicioOdontologo;
    }

    public Turno reservar(Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora) {
        Paciente paciente = servicioPaciente.buscarPorId(pacienteId);
        Odontologo odontologo = servicioOdontologo.buscarPorId(odontologoId);

        if (!odontologo.getAgenda().estaDisponible(fecha, hora)) {
            throw new TurnoYaReservadoException("El odontólogo " + odontologo.getNombreCompleto() +
                    " ya tiene un turno reservado el " + fecha + " a las " + hora);
        }

        Turno turno = new Turno(paciente, odontologo, fecha, hora);
        odontologo.getAgenda().agregarTurno(turno);
        paciente.agregarTurno(turno);
        repositorioTurno.guardar(turno);
        return turno;
    }

    public Turno buscarPorId(Long id) {
        return repositorioTurno.buscarPorId(id)
                .orElseThrow(() -> new TurnoNoEncontradoException("No se encontró turno con ID: " + id));
    }

    public void confirmar(Long turnoId) {
        Turno turno = buscarPorId(turnoId);
        turno.setEstado(EstadoTurno.CONFIRMADO);
    }

    public void cancelar(Long turnoId) {
        Turno turno = buscarPorId(turnoId);
        turno.setEstado(EstadoTurno.CANCELADO);
    }

    public void completar(Long turnoId) {
        Turno turno = buscarPorId(turnoId);
        turno.setEstado(EstadoTurno.COMPLETADO);
    }

    public void modificar(Long turnoId, LocalDate nuevaFecha, LocalTime nuevaHora) {
        Turno turno = buscarPorId(turnoId);
        Odontologo odontologo = turno.getOdontologo();

        if (!odontologo.getAgenda().estaDisponible(nuevaFecha, nuevaHora)) {
            throw new TurnoYaReservadoException("El odontólogo " + odontologo.getNombreCompleto() +
                    " no está disponible el " + nuevaFecha + " a las " + nuevaHora);
        }

        turno.setFecha(nuevaFecha);
        turno.setHora(nuevaHora);
        turno.setEstado(EstadoTurno.PENDIENTE);
        repositorioTurno.actualizar(turno);
    }

    public List<Turno> listarTodos() {
        return repositorioTurno.buscarTodos().stream()
                .sorted(Comparator.comparing(Turno::getFecha).thenComparing(Turno::getHora))
                .collect(Collectors.toList());
    }

    public List<Turno> listarPorPaciente(Long pacienteId) {
        servicioPaciente.buscarPorId(pacienteId);
        return repositorioTurno.buscarPorPacienteId(pacienteId).stream()
                .sorted(Comparator.comparing(Turno::getFecha).thenComparing(Turno::getHora))
                .collect(Collectors.toList());
    }

    public List<Turno> listarPorOdontologo(Long odontologoId) {
        servicioOdontologo.buscarPorId(odontologoId);
        return repositorioTurno.buscarPorOdontologoId(odontologoId).stream()
                .sorted(Comparator.comparing(Turno::getFecha).thenComparing(Turno::getHora))
                .collect(Collectors.toList());
    }

    public List<Turno> listarPorFecha(LocalDate fecha) {
        return repositorioTurno.buscarPorFecha(fecha).stream()
                .sorted(Comparator.comparing(Turno::getHora))
                .collect(Collectors.toList());
    }

    public List<Turno> listarPorEstado(EstadoTurno estado) {
        return repositorioTurno.buscarPorEstado(estado).stream()
                .sorted(Comparator.comparing(Turno::getFecha).thenComparing(Turno::getHora))
                .collect(Collectors.toList());
    }
}
