package clinicasonrisafeliz.excepcion;

/**
 * Se lanza cuando se intenta realizar una operación que no está permitida
 * en el estado actual del sistema.
 * Ejemplo: eliminar un paciente u odontólogo que tiene turnos futuros asignados.
 */
public class OperacionNoPermitidaException extends ClinicaException {

    public OperacionNoPermitidaException(String mensaje) {
        super("OPERACION_NO_PERMITIDA", mensaje);
    }
}
