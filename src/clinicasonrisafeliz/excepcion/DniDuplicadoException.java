package clinicasonrisafeliz.excepcion;

public class DniDuplicadoException extends ClinicaException {
    public DniDuplicadoException(String mensaje) {
        super("DNI_DUPLICADO", mensaje);
    }
}
