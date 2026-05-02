package com.clinica.sonrisafeliz.presentacion.consola;

import com.clinica.sonrisafeliz.controlador.ControladorTurno;
import com.clinica.sonrisafeliz.enums.EstadoTurno;
import com.clinica.sonrisafeliz.modelo.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MenuTurnos {

    private final ControladorTurno controladorTurno;
    private final MenuPacientes menuPacientes;
    private final MenuOdontologos menuOdontologos;
    private final ConsolaUtils utils;

    public MenuTurnos(ControladorTurno controladorTurno,
                      MenuPacientes menuPacientes,
                      MenuOdontologos menuOdontologos,
                      ConsolaUtils utils) {
        this.controladorTurno = controladorTurno;
        this.menuPacientes = menuPacientes;
        this.menuOdontologos = menuOdontologos;
        this.utils = utils;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n========== GESTIÓN DE TURNOS ==========");
            System.out.println("1.  Reservar turno");
            System.out.println("2.  Buscar turno por ID");
            System.out.println("3.  Confirmar turno");
            System.out.println("4.  Cancelar turno");
            System.out.println("5.  Modificar fecha/hora de turno");
            System.out.println("6.  Listar todos los turnos");
            System.out.println("7.  Listar turnos de un paciente");
            System.out.println("8.  Listar turnos de un odontólogo");
            System.out.println("9.  Listar turnos por fecha");
            System.out.println("10. Listar turnos por estado");
            System.out.println("0.  Volver");
            opcion = utils.leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1  -> reservar();
                case 2  -> buscarPorId();
                case 3  -> confirmar();
                case 4  -> cancelar();
                case 5  -> modificar();
                case 6  -> listarTodos();
                case 7  -> listarPorPaciente();
                case 8  -> listarPorOdontologo();
                case 9  -> listarPorFecha();
                case 10 -> listarPorEstado();
                case 0  -> {}
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void reservar() {
        System.out.println("\n--- Reservar Turno ---");
        try {
            menuPacientes.listarTodos();
            Long pacienteId = utils.leerLong("ID del paciente: ");
            menuOdontologos.listarTodos();
            Long odontologoId = utils.leerLong("ID del odontólogo: ");
            LocalDate fecha = utils.leerFecha("Fecha (AAAA-MM-DD): ");
            LocalTime hora = utils.leerHora("Hora (HH:MM): ");
            Turno turno = controladorTurno.reservar(pacienteId, odontologoId, fecha, hora);
            System.out.println("✓ Turno reservado con ID " + turno.getId() + ": " + turno);
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void buscarPorId() {
        try {
            Long id = utils.leerLong("ID del turno: ");
            System.out.println(controladorTurno.buscarPorId(id));
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void confirmar() {
        try {
            Long id = utils.leerLong("ID del turno a confirmar: ");
            controladorTurno.confirmar(id);
            System.out.println("✓ Turno confirmado.");
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void cancelar() {
        try {
            Long id = utils.leerLong("ID del turno a cancelar: ");
            controladorTurno.cancelar(id);
            System.out.println("✓ Turno cancelado.");
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void modificar() {
        try {
            Long id = utils.leerLong("ID del turno a modificar: ");
            LocalDate nuevaFecha = utils.leerFecha("Nueva fecha (AAAA-MM-DD): ");
            LocalTime nuevaHora = utils.leerHora("Nueva hora (HH:MM): ");
            controladorTurno.modificar(id, nuevaFecha, nuevaHora);
            System.out.println("✓ Turno modificado.");
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void listarTodos() {
        imprimir(controladorTurno.listarTodos());
    }

    private void listarPorPaciente() {
        try {
            Long id = utils.leerLong("ID del paciente: ");
            imprimir(controladorTurno.listarPorPaciente(id));
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void listarPorOdontologo() {
        try {
            Long id = utils.leerLong("ID del odontólogo: ");
            imprimir(controladorTurno.listarPorOdontologo(id));
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void listarPorFecha() {
        try {
            LocalDate fecha = utils.leerFecha("Fecha (AAAA-MM-DD): ");
            imprimir(controladorTurno.listarPorFecha(fecha));
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void listarPorEstado() {
        System.out.println("Estados: PENDIENTE, CONFIRMADO, CANCELADO, COMPLETADO");
        try {
            String estadoStr = utils.leerTexto("Estado: ").toUpperCase();
            EstadoTurno estado = EstadoTurno.valueOf(estadoStr);
            imprimir(controladorTurno.listarPorEstado(estado));
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Estado inválido.");
        }
    }

    private void imprimir(List<Turno> lista) {
        if (lista.isEmpty()) {
            System.out.println("No se encontraron turnos.");
            return;
        }
        lista.forEach(t -> System.out.println("  [" + t.getId() + "] " + t));
    }
}
