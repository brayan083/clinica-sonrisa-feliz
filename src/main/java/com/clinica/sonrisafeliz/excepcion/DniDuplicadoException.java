package com.clinica.sonrisafeliz.excepcion;

public class DniDuplicadoException extends RuntimeException {
    public DniDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
