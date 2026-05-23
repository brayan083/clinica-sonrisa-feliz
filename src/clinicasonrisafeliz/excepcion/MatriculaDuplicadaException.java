package clinicasonrisafeliz.excepcion;

public class MatriculaDuplicadaException extends ClinicaException {
    public MatriculaDuplicadaException(String mensaje) {
        super("MATRICULA_DUPLICADA", mensaje);
    }
}
