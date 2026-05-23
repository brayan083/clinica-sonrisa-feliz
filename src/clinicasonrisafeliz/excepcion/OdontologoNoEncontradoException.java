package clinicasonrisafeliz.excepcion;

public class OdontologoNoEncontradoException extends ClinicaException {
    public OdontologoNoEncontradoException(String mensaje) {
        super("ODONTOLOGO_NO_ENCONTRADO", mensaje);
    }
}
