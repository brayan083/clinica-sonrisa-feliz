package clinicasonrisafeliz.controlador;

import clinicasonrisafeliz.excepcion.DatoInvalidoException;
import clinicasonrisafeliz.excepcion.OperacionNoPermitidaException;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.servicio.ServicioOdontologo;
import clinicasonrisafeliz.servicio.ServicioTurno;

import java.util.List;

public class ControladorOdontologo {

    private final ServicioOdontologo servicioOdontologo;
    private final ServicioTurno      servicioTurno;

    public ControladorOdontologo(ServicioOdontologo servicioOdontologo, ServicioTurno servicioTurno) {
        this.servicioOdontologo = servicioOdontologo;
        this.servicioTurno      = servicioTurno;
    }

    public Odontologo registrar(String nombre, String apellido, String email, String matricula) {
        validarTexto(nombre, "nombre");
        validarTexto(apellido, "apellido");
        validarEmail(email);
        validarTexto(matricula, "matrícula");
        return servicioOdontologo.registrar(nombre, apellido, email, matricula);
    }

    public Odontologo buscarPorId(Long id) {
        validarId(id, "odontólogo");
        return servicioOdontologo.buscarPorId(id);
    }

    public Odontologo buscarPorMatricula(String matricula) {
        validarTexto(matricula, "matrícula");
        return servicioOdontologo.buscarPorMatricula(matricula);
    }

    public List<Odontologo> listarTodos() {
        return servicioOdontologo.listarTodos();
    }

    public void actualizar(Long id, String nombre, String apellido, String email) {
        validarId(id, "odontólogo");
        validarTexto(nombre, "nombre");
        validarTexto(apellido, "apellido");
        validarEmail(email);
        servicioOdontologo.actualizar(id, nombre, apellido, email);
    }

    public void eliminar(Long id) {
        validarId(id, "odontólogo");
        Odontologo odontologo = servicioOdontologo.buscarPorId(id);
        List<Turno> turnos = servicioTurno.listarPorOdontologo(id);
        for (Turno t : turnos) {
            if (t.esFuturo()) {
                throw new OperacionNoPermitidaException("No se puede eliminar: el odontólogo " +
                        odontologo.getNombreCompleto() + " tiene turnos futuros asignados.");
            }
        }
        servicioOdontologo.eliminar(id);
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
