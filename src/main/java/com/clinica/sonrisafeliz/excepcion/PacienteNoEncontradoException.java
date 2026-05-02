package com.clinica.sonrisafeliz.excepcion;

public class PacienteNoEncontradoException extends RuntimeException {
    public PacienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
