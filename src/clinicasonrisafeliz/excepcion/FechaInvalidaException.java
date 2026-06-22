package clinicasonrisafeliz.excepcion;

/**
 * Caso especial de DatoInvalidoException para fechas incorrectas
 * (ej: intentar reservar un turno en una fecha pasada).
 */
public class FechaInvalidaException extends DatoInvalidoException {
    public FechaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
