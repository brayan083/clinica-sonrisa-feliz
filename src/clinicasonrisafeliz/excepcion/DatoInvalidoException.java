package clinicasonrisafeliz.excepcion;

/**
 * Se lanza cuando un dato ingresado no cumple las validaciones del negocio.
 * Ejemplos: fecha pasada en un turno, campo vacío obligatorio, formato incorrecto.
 */
public class DatoInvalidoException extends ClinicaException {

    public DatoInvalidoException(String mensaje) {
        super("DATO_INVALIDO", mensaje);
    }
}
