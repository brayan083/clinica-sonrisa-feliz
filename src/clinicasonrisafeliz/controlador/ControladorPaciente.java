package clinicasonrisafeliz.controlador;

import clinicasonrisafeliz.excepcion.DatoInvalidoException;
import clinicasonrisafeliz.excepcion.OperacionNoPermitidaException;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.servicio.ServicioPaciente;
import clinicasonrisafeliz.servicio.ServicioTurno;

import java.util.List;

public class ControladorPaciente {

    private final ServicioPaciente servicioPaciente;
    private final ServicioTurno    servicioTurno;

    public ControladorPaciente(ServicioPaciente servicioPaciente, ServicioTurno servicioTurno) {
        this.servicioPaciente = servicioPaciente;
        this.servicioTurno    = servicioTurno;
    }

    public Paciente registrar(String nombre, String apellido, String email, String dni, Domicilio domicilio) {
        validarTexto(nombre, "nombre");
        validarTexto(apellido, "apellido");
        validarEmail(email);
        validarTexto(dni, "DNI");
        if (domicilio == null) throw new DatoInvalidoException("El domicilio no puede ser nulo.");
        return servicioPaciente.registrar(nombre, apellido, email, dni, domicilio);
    }

    public Paciente buscarPorId(Long id) {
        validarId(id, "paciente");
        return servicioPaciente.buscarPorId(id);
    }

    public Paciente buscarPorDni(String dni) {
        validarTexto(dni, "DNI");
        return servicioPaciente.buscarPorDni(dni);
    }

    public List<Paciente> listarTodos() {
        return servicioPaciente.listarTodos();
    }

    public List<String> listarNombresCompletos() {
        return servicioPaciente.listarNombresCompletos();
    }

    public List<Paciente> buscarPorApellido(String apellido) {
        validarTexto(apellido, "apellido");
        return servicioPaciente.buscarPorApellido(apellido);
    }

    public void actualizar(Long id, String nombre, String apellido, String email) {
        validarId(id, "paciente");
        validarTexto(nombre, "nombre");
        validarTexto(apellido, "apellido");
        validarEmail(email);
        servicioPaciente.actualizar(id, nombre, apellido, email);
    }

    public void eliminar(Long id) {
        validarId(id, "paciente");
        Paciente paciente = servicioPaciente.buscarPorId(id);
        List<Turno> turnos = servicioTurno.listarPorPaciente(id);
        for (Turno t : turnos) {
            if (t.esFuturo()) {
                throw new OperacionNoPermitidaException("No se puede eliminar: el paciente " +
                        paciente.getNombreCompleto() + " tiene turnos futuros asignados.");
            }
        }
        servicioPaciente.eliminar(id);
    }

    // ── Validaciones ─────────────────────────────────────────────────────────

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new DatoInvalidoException("El campo '" + campo + "' no puede estar vacío.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new DatoInvalidoException("El email '" + email + "' no tiene un formato válido.");
        }
    }

    private void validarId(Long id, String entidad) {
        if (id == null || id <= 0) {
            throw new DatoInvalidoException("El ID de " + entidad + " debe ser un número positivo.");
        }
    }
}
