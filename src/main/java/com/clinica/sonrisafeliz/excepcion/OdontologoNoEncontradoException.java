package com.clinica.sonrisafeliz.excepcion;

public class OdontologoNoEncontradoException extends RuntimeException {
    public OdontologoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
