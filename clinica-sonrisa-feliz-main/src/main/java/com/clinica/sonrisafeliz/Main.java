package com.clinica.sonrisafeliz;

import java.time.LocalDate;
import java.time.LocalTime;

import com.clinica.sonrisafeliz.modelo.Domicilio;
import com.clinica.sonrisafeliz.modelo.Enums.EstadoTurno;
import com.clinica.sonrisafeliz.modelo.Odontologo;
import com.clinica.sonrisafeliz.modelo.Paciente;
import com.clinica.sonrisafeliz.modelo.Recepcionista;
import com.clinica.sonrisafeliz.modelo.Turno;

/**
 * Clase de arranque y prueba para la Entrega 1.
 * Demuestra la creación de objetos del modelo de dominio y sus relaciones.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("=======================================================");
        System.out.println("  Clínica Odontológica 'Sonrisa Feliz' - Entrega 1    ");
        System.out.println("=======================================================\n");

        // ── 1. Crear domicilios ────────────────────────────────
        Domicilio domAna    = new Domicilio("Av. Corrientes", "1234", "Buenos Aires", "Buenos Aires");
        Domicilio domCarlos = new Domicilio("San Martín",     "567",  "Córdoba",      "Córdoba");
        Domicilio domLucia  = new Domicilio("Belgrano",       "890",  "Rosario",      "Santa Fe");
        Domicilio domPablo = new Domicilio();
        domPablo.setCalle("Salta");
        domPablo.setNumero("654");
        domPablo.setLocalidad("Monserrat");
        domPablo.setProvincia("Buenos Aires");


        // ── 2. Crear pacientes (composición con Domicilio) ─────
        Paciente ana    = new Paciente("Ana",    "García",    "30111222", "ana@mail.com",    domAna);
        Paciente carlos = new Paciente("Carlos", "Martínez",  "28333444", "carlos@mail.com", domCarlos);
        Paciente lucia  = new Paciente("Lucía",  "Fernández", "35555666", "lucia@mail.com",  domLucia);
        Paciente lucia2 = new Paciente("Lucía",  "Fernández", "35555666", "lucia@mail.com", LocalDate.of(2024, 1, 15), domLucia);
        Paciente pablo = new Paciente("Pablo", "Lopez", "9566631", "pablolopez21@mail.com", domPablo);

        // ── 3. Crear odontólogos ───────────────────────────────
        Odontologo drLopez    = new Odontologo("Diego",   "López",   "diego@clinica.com",   "MAT-001");
        Odontologo draSanchez = new Odontologo("Valeria", "Sánchez", "valeria@clinica.com", "MAT-002");

        // ── 4. Crear recepcionista ─────────────────────────────
        Recepcionista recepcionista = new Recepcionista("María", "Gómez", "maria@clinica.com", "LEG-01");

        // ── 5. Crear turnos vía recepcionista ──────────────────
        Turno turno1 = recepcionista.asignarTurno(ana,    drLopez,    LocalDate.of(2026, 4, 15), LocalTime.of(9,  0));
        Turno turno2 = recepcionista.asignarTurno(carlos, draSanchez, LocalDate.of(2026, 4, 15), LocalTime.of(10, 30));
        Turno turno3 = recepcionista.asignarTurno(lucia,  drLopez,    LocalDate.of(2026, 4, 16), LocalTime.of(14, 0));
        Turno turnoPasado = recepcionista.asignarTurno(ana, draSanchez, LocalDate.of(2026, 3, 1), LocalTime.of(11, 0));
        Turno turno4 = recepcionista.asignarTurno(pablo, draSanchez, LocalDate.of(2026, 4, 16), LocalTime.of(13, 25));

        // Ajustamos estados que difieren del PENDIENTE inicial.
        turno2.setEstado(EstadoTurno.CONFIRMADO);
        turnoPasado.setEstado(EstadoTurno.COMPLETADO);

        // ── 6. Imprimir pacientes ──────────────────────────────
        System.out.println("── PACIENTES ─────────────────────────────────────────");
        imprimirPaciente(ana);
        imprimirPaciente(carlos);
        imprimirPaciente(lucia);
        imprimirPaciente(lucia2);
        imprimirPaciente(pablo);

        // ── 7. Imprimir odontólogos ────────────────────────────
        System.out.println("\n── ODONTÓLOGOS ───────────────────────────────────────");
        System.out.println(drLopez);
        System.out.println(draSanchez);

        // ── 8. Imprimir recepcionista ──────────────────────────
        System.out.println("\n── RECEPCIONISTA ─────────────────────────────────────");
        System.out.println(recepcionista);

        // ── 9. Imprimir turnos ─────────────────────────────────
        System.out.println("\n── TURNOS ────────────────────────────────────────────");
        imprimirTurno(turno1);
        imprimirTurno(turno2);
        imprimirTurno(turno3);
        imprimirTurno(turno4);

        // ── 10. Verificar métodos de negocio ───────────────────
        System.out.println("\n── VERIFICACIÓN DE MÉTODOS DE NEGOCIO ────────────────");
        System.out.println("Nombre completo Ana:      " + ana.getNombreCompleto());
        System.out.println("Nombre completo Dr López: " + drLopez.getNombreCompleto());
        System.out.println("turno1 es futuro:         " + turno1.esFuturo());
        System.out.println("turno1 está disponible:   " + turno1.estaDisponible());
        System.out.println("turnoPasado es futuro:    " + turnoPasado.esFuturo());
        System.out.println("turnoPasado disponible:   " + turnoPasado.estaDisponible());

        // ── 11. Verificar equals/hashCode por DNI ──────────────
        System.out.println("\n── VERIFICACIÓN DE EQUALS (identidad de negocio) ─────");
        Paciente anaDuplicada = new Paciente("Ana", "García", "30111222", "otro@mail.com", domAna);
        System.out.println("ana.equals(anaDuplicada) [mismo DNI]: " + ana.equals(anaDuplicada));
        System.out.println("ana.equals(carlos)       [DNI dif.]: " + ana.equals(carlos));

        // ── 12. Estados del ciclo de vida ──────────────────────
        System.out.println("\n── CICLO DE VIDA DEL TURNO ───────────────────────────");
        System.out.println("Estado inicial turno3: " + turno3.getEstado());
        turno3.setEstado(EstadoTurno.CONFIRMADO);
        System.out.println("Después de confirmar:  " + turno3.getEstado());
        turno3.setEstado(EstadoTurno.COMPLETADO);
        System.out.println("Después de completar:  " + turno3.getEstado());

        // ── 13. Turno con toString ──────────────────────
        System.out.println("\n── TURNO ───────────────────────────");
        System.out.println(pablo);
        System.out.println(domPablo);
    }

    // ── Helpers de impresión ───────────────────────────────────

    private static void imprimirPaciente(Paciente p) {
        System.out.println("  → " + p.getNombreCompleto()
                + " | DNI: " + p.getDni()
                + " | Ingreso: " + p.getFechaIngreso()
                + " | Dom.: " + p.getDomicilio());
    }

    private static void imprimirTurno(Turno t) {
        System.out.println("  → Turno #" + t.getId()
                + " | " + t.getPaciente().getNombreCompleto()
                + " con Dr/a. " + t.getOdontologo().getNombreCompleto()
                + " | " + t.getFecha() + " " + t.getHora()
                + " | Estado: " + t.getEstado()
                + " | Futuro: " + t.esFuturo());
    }
}
