package com.clinica.sonrisafeliz.excepcion;

public class FechaInvalidaException extends RuntimeException {
    public FechaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
