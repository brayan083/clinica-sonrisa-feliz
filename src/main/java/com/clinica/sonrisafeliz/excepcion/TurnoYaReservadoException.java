package com.clinica.sonrisafeliz.excepcion;

public class TurnoYaReservadoException extends RuntimeException {
    public TurnoYaReservadoException(String mensaje) {
        super(mensaje);
    }
}
