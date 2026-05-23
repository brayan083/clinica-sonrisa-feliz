package clinicasonrisafeliz.controlador;

import clinicasonrisafeliz.enums.EstadoTurno;
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
        return servicioTurno.reservar(pacienteId, odontologoId, fecha, hora);
    }

    public Turno buscarPorId(Long id) {
        return servicioTurno.buscarPorId(id);
    }

    public void confirmar(Long turnoId) {
        servicioTurno.confirmar(turnoId);
    }

    public void cancelar(Long turnoId) {
        servicioTurno.cancelar(turnoId);
    }

    public void completar(Long turnoId) {
        servicioTurno.completar(turnoId);
    }

    public void modificar(Long turnoId, LocalDate nuevaFecha, LocalTime nuevaHora) {
        servicioTurno.modificar(turnoId, nuevaFecha, nuevaHora);
    }

    public List<Turno> listarTodos() {
        return servicioTurno.listarTodos();
    }

    public List<Turno> listarPorPaciente(Long pacienteId) {
        return servicioTurno.listarPorPaciente(pacienteId);
    }

    public List<Turno> listarPorOdontologo(Long odontologoId) {
        return servicioTurno.listarPorOdontologo(odontologoId);
    }

    public List<Turno> listarPorFecha(LocalDate fecha) {
        return servicioTurno.listarPorFecha(fecha);
    }

    public List<Turno> listarPorEstado(EstadoTurno estado) {
        return servicioTurno.listarPorEstado(estado);
    }

    public List<Turno> listarPorRangoDeFechas(LocalDate desde, LocalDate hasta) {
        return servicioTurno.listarPorRangoDeFechas(desde, hasta);
    }
}
