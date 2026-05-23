package clinicasonrisafeliz.presentacion.consola;

import clinicasonrisafeliz.servicio.ServicioPersistencia;

import java.util.Scanner;

public class MenuPrincipal {

    private final MenuPacientes       menuPacientes;
    private final MenuOdontologos     menuOdontologos;
    private final MenuTurnos          menuTurnos;
    private final ServicioPersistencia servicioPersistencia;
    private final ConsolaUtils        utils;

    public MenuPrincipal(MenuPacientes menuPacientes,
                         MenuOdontologos menuOdontologos,
                         MenuTurnos menuTurnos,
                         ServicioPersistencia servicioPersistencia) {
        this.menuPacientes        = menuPacientes;
        this.menuOdontologos      = menuOdontologos;
        this.menuTurnos           = menuTurnos;
        this.servicioPersistencia = servicioPersistencia;
        this.utils                = new ConsolaUtils(new Scanner(System.in));
    }

    public void iniciar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║   CLÍNICA SONRISA FELIZ      ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. Pacientes                ║");
            System.out.println("║  2. Odontólogos              ║");
            System.out.println("║  3. Turnos                   ║");
            System.out.println("║  4. Guardar datos            ║");
            System.out.println("║  0. Salir                    ║");
            System.out.println("╚══════════════════════════════╝");
            opcion = utils.leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> menuPacientes.mostrar();
                case 2 -> menuOdontologos.mostrar();
                case 3 -> menuTurnos.mostrar();
                case 4 -> guardarDatos();
                case 0 -> System.out.println("\n¡Hasta luego!");
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    private void guardarDatos() {
        try {
            servicioPersistencia.guardar();
            System.out.println("✓ Datos guardados correctamente en archivos CSV.");
        } catch (Exception e) {
            System.out.println("✗ Error al guardar: " + e.getMessage());
        }
    }
}
