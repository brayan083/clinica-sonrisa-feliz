package clinicasonrisafeliz.excepcion;

public class TurnoYaReservadoException extends ClinicaException {
    public TurnoYaReservadoException(String mensaje) {
        super("TURNO_YA_RESERVADO", mensaje);
    }
}
