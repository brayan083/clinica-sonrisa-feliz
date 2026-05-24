package clinicasonrisafeliz.controlador;

import clinicasonrisafeliz.excepcion.DatoInvalidoException;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.servicio.ServicioOdontologo;

import java.util.List;

public class ControladorOdontologo {

    private final ServicioOdontologo servicioOdontologo;

    public ControladorOdontologo(ServicioOdontologo servicioOdontologo) {
        this.servicioOdontologo = servicioOdontologo;
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
        servicioOdontologo.eliminar(id);
    }

    // ── Validaciones de formato y nulidad ────────────────────────────────────

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
