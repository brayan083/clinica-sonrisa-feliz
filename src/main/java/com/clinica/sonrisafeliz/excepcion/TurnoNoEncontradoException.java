package com.clinica.sonrisafeliz.excepcion;

public class TurnoNoEncontradoException extends RuntimeException {
    public TurnoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
