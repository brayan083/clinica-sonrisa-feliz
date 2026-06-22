package clinicasonrisafeliz.excepcion;

public class TurnoNoEncontradoException extends ClinicaException {
    public TurnoNoEncontradoException(String mensaje) {
        super("TURNO_NO_ENCONTRADO", mensaje);
    }
}
