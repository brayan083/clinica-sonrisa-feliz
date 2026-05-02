package com.clinica.sonrisafeliz.presentacion.consola;

import com.clinica.sonrisafeliz.controlador.ControladorOdontologo;
import com.clinica.sonrisafeliz.modelo.Odontologo;

import java.util.List;

public class MenuOdontologos {

    private final ControladorOdontologo controladorOdontologo;
    private final ConsolaUtils utils;

    public MenuOdontologos(ControladorOdontologo controladorOdontologo, ConsolaUtils utils) {
        this.controladorOdontologo = controladorOdontologo;
        this.utils = utils;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n========== GESTIÓN DE ODONTÓLOGOS ==========");
            System.out.println("1. Registrar odontólogo");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar por matrícula");
            System.out.println("4. Listar todos");
            System.out.println("5. Modificar odontólogo");
            System.out.println("6. Eliminar odontólogo");
            System.out.println("0. Volver");
            opcion = utils.leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> registrar();
                case 2 -> buscarPorId();
                case 3 -> buscarPorMatricula();
                case 4 -> listarTodos();
                case 5 -> modificar();
                case 6 -> eliminar();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    public void listarTodos() {
        List<Odontologo> lista = controladorOdontologo.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay odontólogos registrados.");
            return;
        }
        System.out.println("\n--- Odontólogos (orden alfabético) ---");
        lista.forEach(o -> System.out.println("  [" + o.getId() + "] Dr/Dra. " + o.getNombreCompleto() + " - Mat: " + o.getMatricula()));
    }

    private void registrar() {
        System.out.println("\n--- Registrar Odontólogo ---");
        try {
            String nombre = utils.leerTexto("Nombre: ");
            String apellido = utils.leerTexto("Apellido: ");
            String email = utils.leerTexto("Email: ");
            String matricula = utils.leerTexto("Matrícula: ");
            Odontologo o = controladorOdontologo.registrar(nombre, apellido, email, matricula);
            System.out.println("✓ Odontólogo registrado con ID " + o.getId() + ": " + o.getNombreCompleto());
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void buscarPorId() {
        try {
            Long id = utils.leerLong("ID del odontólogo: ");
            System.out.println(controladorOdontologo.buscarPorId(id));
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void buscarPorMatricula() {
        try {
            String matricula = utils.leerTexto("Matrícula: ");
            System.out.println(controladorOdontologo.buscarPorMatricula(matricula));
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void modificar() {
        try {
            Long id = utils.leerLong("ID del odontólogo a modificar: ");
            Odontologo o = controladorOdontologo.buscarPorId(id);
            System.out.println("(Deje en blanco para no modificar)");
            String nombre = utils.leerTextoOpcional("Nuevo nombre [" + o.getNombre() + "]: ", o.getNombre());
            String apellido = utils.leerTextoOpcional("Nuevo apellido [" + o.getApellido() + "]: ", o.getApellido());
            String email = utils.leerTextoOpcional("Nuevo email [" + o.getEmail() + "]: ", o.getEmail());
            o.setNombre(nombre);
            o.setApellido(apellido);
            o.setEmail(email);
            controladorOdontologo.actualizar(o);
            System.out.println("✓ Odontólogo actualizado.");
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void eliminar() {
        try {
            Long id = utils.leerLong("ID del odontólogo a eliminar: ");
            controladorOdontologo.eliminar(id);
            System.out.println("✓ Odontólogo eliminado.");
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }
}
