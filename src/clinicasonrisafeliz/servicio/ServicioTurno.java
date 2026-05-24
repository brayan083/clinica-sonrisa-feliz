package clinicasonrisafeliz.servicio;

import clinicasonrisafeliz.enums.EstadoTurno;
import clinicasonrisafeliz.excepcion.DatoInvalidoException;
import clinicasonrisafeliz.excepcion.FechaInvalidaException;
import clinicasonrisafeliz.excepcion.TurnoNoEncontradoException;
import clinicasonrisafeliz.excepcion.TurnoYaReservadoException;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.repositorio.RepositorioTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
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
        this.repositorioTurno   = repositorioTurno;
        this.servicioPaciente   = servicioPaciente;
        this.servicioOdontologo = servicioOdontologo;
    }

    public Turno reservar(Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora) {
        if (fecha.isBefore(LocalDate.now())) {
            throw new FechaInvalidaException("No se puede reservar un turno en una fecha pasada: " + fecha);
        }

        Paciente   paciente   = servicioPaciente.buscarPorId(pacienteId);
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
        Turno turno = repositorioTurno.buscarPorId(id);
        if (turno == null) {
            throw new TurnoNoEncontradoException("No se encontró turno con ID: " + id);
        }
        return turno;
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

    /**
     * Lista todos los turnos por orden natural (fecha y hora).
     * Usa Collections.sort() con Comparable<Turno>.
     */
    public List<Turno> listarTodos() {
        List<Turno> lista = repositorioTurno.buscarTodos();
        Collections.sort(lista);
        return lista;
    }

    /**
     * Filtra los turnos de un paciente y los ordena con Stream API + sorted() (Comparable).
     */
    public List<Turno> listarPorPaciente(Long pacienteId) {
        servicioPaciente.buscarPorId(pacienteId);
        return repositorioTurno.buscarPorPacienteId(pacienteId)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Filtra los turnos de un odontólogo y los ordena con Stream API + sorted() (Comparable).
     */
    public List<Turno> listarPorOdontologo(Long odontologoId) {
        servicioOdontologo.buscarPorId(odontologoId);
        return repositorioTurno.buscarPorOdontologoId(odontologoId)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Lista turnos de una fecha específica ordenados por hora.
     * Usa Stream API + sorted(Comparator) para orden personalizado.
     */
    public List<Turno> listarPorFecha(LocalDate fecha) {
        return repositorioTurno.buscarPorFecha(fecha)
                .stream()
                .sorted(Comparator.comparing(Turno::getHora))
                .collect(Collectors.toList());
    }

    /**
     * Filtra turnos por estado y ordena con Stream API + sorted() (Comparable).
     */
    public List<Turno> listarPorEstado(EstadoTurno estado) {
        return repositorioTurno.buscarPorEstado(estado)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Busca turnos en el rango [desde, hasta] inclusive.
     * Usa Stream API: filter (predicado de fechas) + sorted (Comparable) + collect.
     */
    public List<Turno> listarPorRangoDeFechas(LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta)) {
            throw new DatoInvalidoException(
                    "La fecha de inicio (" + desde + ") no puede ser posterior a la fecha fin (" + hasta + ").");
        }
        return repositorioTurno.buscarTodos()
                .stream()
                .filter(t -> !t.getFecha().isBefore(desde) && !t.getFecha().isAfter(hasta))
                .sorted()
                .collect(Collectors.toList());
    }
}
