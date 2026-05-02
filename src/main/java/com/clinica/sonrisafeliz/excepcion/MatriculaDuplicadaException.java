package com.clinica.sonrisafeliz.excepcion;

public class MatriculaDuplicadaException extends RuntimeException {
    public MatriculaDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
