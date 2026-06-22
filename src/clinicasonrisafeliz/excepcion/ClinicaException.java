package clinicasonrisafeliz.excepcion;

/**
 * Clase base de todas las excepciones del dominio de la Clínica Sonrisa Feliz.
 * Centraliza el manejo de errores propios del negocio y permite capturarlos
 * de forma genérica en la capa de presentación con un solo catch.
 */
public class ClinicaException extends RuntimeException {

    private final String codigo;

    public ClinicaException(String mensaje) {
        super(mensaje);
        this.codigo = "CLINICA_ERROR";
    }

    public ClinicaException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    @Override
    public String toString() {
        return "[" + codigo + "] " + getMessage();
    }
}
