package clinicasonrisafeliz.excepcion;

public class PacienteNoEncontradoException extends ClinicaException {
    public PacienteNoEncontradoException(String mensaje) {
        super("PACIENTE_NO_ENCONTRADO", mensaje);
    }
}
