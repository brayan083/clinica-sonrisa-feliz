package com.clinica.sonrisafeliz.presentacion.consola;

import com.clinica.sonrisafeliz.controlador.ControladorPaciente;
import com.clinica.sonrisafeliz.modelo.Domicilio;
import com.clinica.sonrisafeliz.modelo.Paciente;

import java.util.List;

public class MenuPacientes {

    private final ControladorPaciente controladorPaciente;
    private final ConsolaUtils utils;

    public MenuPacientes(ControladorPaciente controladorPaciente, ConsolaUtils utils) {
        this.controladorPaciente = controladorPaciente;
        this.utils = utils;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n========== GESTIÓN DE PACIENTES ==========");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar por DNI");
            System.out.println("4. Listar todos");
            System.out.println("5. Modificar paciente");
            System.out.println("6. Eliminar paciente");
            System.out.println("0. Volver");
            opcion = utils.leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> registrar();
                case 2 -> buscarPorId();
                case 3 -> buscarPorDni();
                case 4 -> listarTodos();
                case 5 -> modificar();
                case 6 -> eliminar();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    public void listarTodos() {
        List<Paciente> lista = controladorPaciente.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        System.out.println("\n--- Pacientes (orden alfabético) ---");
        lista.forEach(p -> System.out.println("  [" + p.getId() + "] " + p.getNombreCompleto() + " - DNI: " + p.getDni()));
    }

    private void registrar() {
        System.out.println("\n--- Registrar Paciente ---");
        try {
            String nombre = utils.leerTexto("Nombre: ");
            String apellido = utils.leerTexto("Apellido: ");
            String email = utils.leerTexto("Email: ");
            String dni = utils.leerTexto("DNI: ");
            System.out.println("  [Domicilio]");
            String calle = utils.leerTexto("  Calle: ");
            String numero = utils.leerTexto("  Número: ");
            String localidad = utils.leerTexto("  Localidad: ");
            String provincia = utils.leerTexto("  Provincia: ");
            Domicilio domicilio = new Domicilio(calle, numero, localidad, provincia);
            Paciente p = controladorPaciente.registrar(nombre, apellido, email, dni, domicilio);
            System.out.println("✓ Paciente registrado con ID " + p.getId() + ": " + p.getNombreCompleto());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void buscarPorId() {
        try {
            Long id = utils.leerLong("ID del paciente: ");
            System.out.println(controladorPaciente.buscarPorId(id));
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void buscarPorDni() {
        try {
            String dni = utils.leerTexto("DNI: ");
            System.out.println(controladorPaciente.buscarPorDni(dni));
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void modificar() {
        try {
            Long id = utils.leerLong("ID del paciente a modificar: ");
            Paciente p = controladorPaciente.buscarPorId(id);
            System.out.println("Datos actuales: " + p);
            System.out.println("(Deje en blanco para no modificar)");
            String nombre = utils.leerTextoOpcional("Nuevo nombre [" + p.getNombre() + "]: ", p.getNombre());
            String apellido = utils.leerTextoOpcional("Nuevo apellido [" + p.getApellido() + "]: ", p.getApellido());
            String email = utils.leerTextoOpcional("Nuevo email [" + p.getEmail() + "]: ", p.getEmail());
            p.setNombre(nombre);
            p.setApellido(apellido);
            p.setEmail(email);
            controladorPaciente.actualizar(p);
            System.out.println("✓ Paciente actualizado.");
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void eliminar() {
        try {
            Long id = utils.leerLong("ID del paciente a eliminar: ");
            controladorPaciente.eliminar(id);
            System.out.println("✓ Paciente eliminado.");
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }
}
