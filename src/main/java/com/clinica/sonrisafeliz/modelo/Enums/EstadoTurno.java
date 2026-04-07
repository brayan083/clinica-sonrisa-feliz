package com.clinica.sonrisafeliz.modelo.Enums;

/**
 * Modela el ciclo de vida de un turno en la clínica.
 *
 * Transiciones válidas:
 *   PENDIENTE  → CONFIRMADO  (recepcionista confirma el turno)
 *   PENDIENTE  → CANCELADO   (paciente o clínica cancela)
 *   CONFIRMADO → COMPLETADO  (turno se realizó)
 *   CONFIRMADO → CANCELADO   (cancelación de un turno ya confirmado)
 */
public enum EstadoTurno {
    PENDIENTE,
    CONFIRMADO,
    CANCELADO,
    COMPLETADO
}
