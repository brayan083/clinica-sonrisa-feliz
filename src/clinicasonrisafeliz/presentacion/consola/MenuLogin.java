package clinicasonrisafeliz.presentacion.consola;

import clinicasonrisafeliz.controlador.ControladorRecepcionista;
import clinicasonrisafeliz.modelo.Recepcionista;

public class MenuLogin {

    private final ControladorRecepcionista controlador;
    private final ConsolaUtils             utils;

    public MenuLogin(ControladorRecepcionista controlador, ConsolaUtils utils) {
        this.controlador = controlador;
        this.utils       = utils;
    }

    public Recepcionista iniciar() {
        Recepcionista r = null;
        while (r == null) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║      CLÍNICA SONRISA FELIZ       ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  1. Iniciar sesión               ║");
            System.out.println("║  2. Registrarse                  ║");
            System.out.println("╚══════════════════════════════════╝");
            int opcion = utils.leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> r = login();
                case 2 -> r = registrar();
                default -> System.out.println("Opción inválida.");
            }
        }
        return r;
    }

    private Recepcionista registrar() {
        System.out.println("\n--- Registro de Recepcionista ---");
        String nombre   = utils.leerTexto("Nombre: ");
        String apellido = utils.leerTexto("Apellido: ");
        String email    = utils.leerTexto("Email: ");
        String legajo   = utils.leerTexto("Legajo: ");
        try {
            Recepcionista r = controlador.registrar(nombre, apellido, email, legajo);
            System.out.println("✓ Registro exitoso. Bienvenido/a, " + r.getNombreCompleto() + ".");
            return r;
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
            return null;
        }
    }

    private Recepcionista login() {
        System.out.println("\n--- Iniciar Sesión ---");
        String legajo = utils.leerTexto("Legajo: ");
        Recepcionista r = controlador.buscarPorLegajo(legajo);
        if (r == null) {
            System.out.println("✗ Legajo no encontrado.");
            return null;
        }
        System.out.println("✓ Bienvenido/a, " + r.getNombreCompleto() + ".");
        return r;
    }
}
