package com.clinica.sonrisafeliz;

import com.clinica.sonrisafeliz.modelo.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal para probar el Modelo de Dominio en la Entrega 1.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA SONRISA FELIZ ===\n");

        // 1. Creamos el "Repositorio" simulado (Lista general de turnos de la clínica)
        List<Turno> historialGeneralTurnos = new ArrayList<>();

        // 2. Creamos a nuestra Recepcionista
        Recepcionista marta = new Recepcionista("Marta", "Gómez", "marta@clinica.com", "REC-001");

        // 3. Creamos a nuestro Odontólogo
        Odontologo drPerez = new Odontologo("Carlos", "Pérez", "cperez@clinica.com", "MAT-9988");

        System.out.println("--- REGISTRO DE PACIENTE ---");
        // 4. Marta registra un domicilio y un paciente
        Domicilio domJuan = new Domicilio("Av. Siempreviva", "742", "Springfield", "Buenos Aires");
        Paciente pacienteJuan = marta.registrarPaciente("Juan", "Topo", "juan@mail.com", "12345678", domJuan);
        System.out.println(pacienteJuan.toString() + "\n");

        System.out.println("--- ASIGNACIÓN DE TURNO ---");
        // 5. Marta intenta asignar un turno para Juan con el Dr. Pérez
        LocalDate fechaTurno = LocalDate.of(2026, 4, 20);
        LocalTime horaTurno = LocalTime.of(10, 30);

        Turno turnoJuan = marta.asignarTurno(pacienteJuan, drPerez, fechaTurno, horaTurno);

        // Si se creó con éxito, lo guardamos en la lista general del sistema
        if(turnoJuan != null) {
            historialGeneralTurnos.add(turnoJuan);
            System.out.println(turnoJuan.toString() + "\n");
        }

        System.out.println("--- PRUEBA DE RESTRICCIÓN DE AGENDA ---");
        // 6. Probamos qué pasa si creamos otro paciente e intentamos meterlo a la misma hora
        Paciente pacienteMaria = marta.registrarPaciente("María", "López", "maria@mail.com", "87654321", domJuan);
        Turno turnoFallido = marta.asignarTurno(pacienteMaria, drPerez, fechaTurno, horaTurno);
        if (turnoFallido != null) {
            historialGeneralTurnos.add(turnoFallido);
        }
        System.out.println();

        System.out.println("--- MODIFICACIÓN Y CANCELACIÓN ---");
        // 7. Marta cancela el turno de Juan porque se enfermó
        // 8. Como el turno de Juan se canceló, ahora ese horario DEBERÍA estar libre para María
        if (turnoJuan != null) {
            marta.cancelarTurno(turnoJuan);
            System.out.println("Estado actual del turno de Juan: " + turnoJuan.getEstado());

            System.out.println("\nReintentando turno para María en el mismo horario...");
            Turno turnoMaria = marta.asignarTurno(pacienteMaria, drPerez, fechaTurno, horaTurno);
            if (turnoMaria != null) {
                historialGeneralTurnos.add(turnoMaria);
            }
        }

        System.out.println("\n--- HISTORIAL DE TURNOS ---");
        historialGeneralTurnos.forEach(t -> System.out.println(t));

        System.out.println("\n=== FIN DE LA DEMO ===");
    }
}