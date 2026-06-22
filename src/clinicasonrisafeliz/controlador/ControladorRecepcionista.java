package clinicasonrisafeliz.controlador;

import clinicasonrisafeliz.excepcion.DatoInvalidoException;
import clinicasonrisafeliz.modelo.Recepcionista;
import clinicasonrisafeliz.servicio.ServicioRecepcionista;

public class ControladorRecepcionista {

    private final ServicioRecepcionista servicioRecepcionista;

    public ControladorRecepcionista(ServicioRecepcionista servicioRecepcionista) {
        this.servicioRecepcionista = servicioRecepcionista;
    }

    public Recepcionista registrar(String nombre, String apellido, String email, String legajo) {
        validarTexto(nombre, "nombre");
        validarTexto(apellido, "apellido");
        validarEmail(email);
        validarTexto(legajo, "legajo");
        return servicioRecepcionista.registrar(nombre, apellido, email, legajo);
    }

    public Recepcionista buscarPorLegajo(String legajo) {
        validarTexto(legajo, "legajo");
        return servicioRecepcionista.buscarPorLegajo(legajo);
    }

    public boolean hayRecepcionistas() {
        return servicioRecepcionista.hayRecepcionistas();
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
}
