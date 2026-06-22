package clinicasonrisafeliz.servicio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import clinicasonrisafeliz.enums.EstadoTurno;
import clinicasonrisafeliz.excepcion.DatoInvalidoException;
import clinicasonrisafeliz.excepcion.FechaInvalidaException;
import clinicasonrisafeliz.excepcion.TurnoNoEncontradoException;
import clinicasonrisafeliz.excepcion.TurnoYaReservadoException;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Recepcionista;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.repositorio.RepositorioTurno;

public class ServicioTurno {

    private final RepositorioTurno repositorioTurno;

    public ServicioTurno(RepositorioTurno repositorioTurno) {
        this.repositorioTurno = repositorioTurno;
    }

    public Turno reservar(Paciente paciente, Odontologo odontologo, LocalDate fecha, LocalTime hora, Recepcionista recepcionista) {
        if (fecha.isBefore(LocalDate.now())) {
            throw new FechaInvalidaException("No se puede reservar un turno en una fecha pasada: " + fecha);
        }
        if (!odontologo.getAgenda().estaDisponible(fecha, hora)) {
            throw new TurnoYaReservadoException("El odontólogo " + odontologo.getNombreCompleto() +
                    " ya tiene un turno reservado el " + fecha + " a las " + hora);
        }
        Turno turno = new Turno(paciente, odontologo, fecha, hora, recepcionista);
        odontologo.getAgenda().agregarTurno(turno);
        repositorioTurno.guardar(turno);
        return turno;
    }

    public Turno buscarPorId(Long id) {
        Turno turno = repositorioTurno.buscarPorId(id);
        if (turno == null) {
            throw new TurnoNoEncontradoException("No se encontró turno con ID: " + id);
        }
        return turno;
    }

    public void confirmar(Long turnoId) {
        Turno turno = buscarPorId(turnoId);
        turno.setEstado(EstadoTurno.CONFIRMADO);
        repositorioTurno.actualizar(turno);
    }

    public void cancelar(Long turnoId) {
        Turno turno = buscarPorId(turnoId);
        turno.setEstado(EstadoTurno.CANCELADO);
        repositorioTurno.actualizar(turno);
    }

    public void completar(Long turnoId) {
        Turno turno = buscarPorId(turnoId);
        turno.setEstado(EstadoTurno.COMPLETADO);
        repositorioTurno.actualizar(turno);
    }

    public void modificar(Long turnoId, LocalDate nuevaFecha, LocalTime nuevaHora) {
        if (nuevaFecha.isBefore(LocalDate.now())) {
            throw new FechaInvalidaException("No se puede modificar un turno a una fecha pasada: " + nuevaFecha);
        }
        Turno      turno      = buscarPorId(turnoId);
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
        List<Turno> lista = repositorioTurno.buscarTodos();
        Collections.sort(lista);
        return lista;
    }

    public List<Turno> listarPorPaciente(Long pacienteId) {
        return repositorioTurno.buscarPorPacienteId(pacienteId)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Turno> listarPorOdontologo(Long odontologoId) {
        return repositorioTurno.buscarPorOdontologoId(odontologoId)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Turno> listarPorFecha(LocalDate fecha) {
        return repositorioTurno.buscarPorFecha(fecha)
                .stream()
                .sorted(Comparator.comparing(Turno::getHora))
                .collect(Collectors.toList());
    }

    public List<Turno> listarPorEstado(EstadoTurno estado) {
        return repositorioTurno.buscarPorEstado(estado)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Turno> listarPorRangoDeFechas(LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta)) {
            throw new DatoInvalidoException(
                    "La fecha de inicio (" + desde + ") no puede ser posterior a la fecha fin (" + hasta + ").");
        }
        return repositorioTurno.buscarPorRangoDeFechas(desde, hasta)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
