package clinicasonrisafeliz.controlador;

import clinicasonrisafeliz.enums.EstadoTurno;
import clinicasonrisafeliz.excepcion.DatoInvalidoException;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.servicio.ServicioTurno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ControladorTurno {

    private final ServicioTurno servicioTurno;

    public ControladorTurno(ServicioTurno servicioTurno) {
        this.servicioTurno = servicioTurno;
    }

    public Turno reservar(Long pacienteId, Long odontologoId, LocalDate fecha, LocalTime hora) {
        validarId(pacienteId, "paciente");
        validarId(odontologoId, "odontólogo");
        if (fecha == null) throw new DatoInvalidoException("La fecha del turno no puede ser nula.");
        if (hora  == null) throw new DatoInvalidoException("La hora del turno no puede ser nula.");
        return servicioTurno.reservar(pacienteId, odontologoId, fecha, hora);
    }

    public Turno buscarPorId(Long id) {
        validarId(id, "turno");
        return servicioTurno.buscarPorId(id);
    }

    public void confirmar(Long turnoId) {
        validarId(turnoId, "turno");
        servicioTurno.confirmar(turnoId);
    }

    public void cancelar(Long turnoId) {
        validarId(turnoId, "turno");
        servicioTurno.cancelar(turnoId);
    }

    public void completar(Long turnoId) {
        validarId(turnoId, "turno");
        servicioTurno.completar(turnoId);
    }

    public void modificar(Long turnoId, LocalDate nuevaFecha, LocalTime nuevaHora) {
        validarId(turnoId, "turno");
        if (nuevaFecha == null) throw new DatoInvalidoException("La nueva fecha no puede ser nula.");
        if (nuevaHora  == null) throw new DatoInvalidoException("La nueva hora no puede ser nula.");
        servicioTurno.modificar(turnoId, nuevaFecha, nuevaHora);
    }

    public List<Turno> listarTodos() {
        return servicioTurno.listarTodos();
    }

    public List<Turno> listarPorPaciente(Long pacienteId) {
        validarId(pacienteId, "paciente");
        return servicioTurno.listarPorPaciente(pacienteId);
    }

    public List<Turno> listarPorOdontologo(Long odontologoId) {
        validarId(odontologoId, "odontólogo");
        return servicioTurno.listarPorOdontologo(odontologoId);
    }

    public List<Turno> listarPorFecha(LocalDate fecha) {
        if (fecha == null) throw new DatoInvalidoException("La fecha de búsqueda no puede ser nula.");
        return servicioTurno.listarPorFecha(fecha);
    }

    public List<Turno> listarPorEstado(EstadoTurno estado) {
        if (estado == null) throw new DatoInvalidoException("El estado no puede ser nulo.");
        return servicioTurno.listarPorEstado(estado);
    }

    public List<Turno> listarPorRangoDeFechas(LocalDate desde, LocalDate hasta) {
        if (desde == null) throw new DatoInvalidoException("La fecha de inicio no puede ser nula.");
        if (hasta == null) throw new DatoInvalidoException("La fecha de fin no puede ser nula.");
        return servicioTurno.listarPorRangoDeFechas(desde, hasta);
    }

    // ── Validaciones de formato y nulidad ────────────────────────────────────

    private void validarId(Long id, String entidad) {
        if (id == null || id <= 0) {
            throw new DatoInvalidoException("El ID de " + entidad + " debe ser un número positivo.");
        }
    }
}
