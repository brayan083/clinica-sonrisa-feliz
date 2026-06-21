package clinicasonrisafeliz;

import java.util.Scanner;

import clinicasonrisafeliz.modelo.Recepcionista;
import clinicasonrisafeliz.presentacion.consola.ConsolaUtils;
import clinicasonrisafeliz.presentacion.consola.MenuLogin;
import clinicasonrisafeliz.presentacion.consola.MenuOdontologos;
import clinicasonrisafeliz.presentacion.consola.MenuPacientes;
import clinicasonrisafeliz.presentacion.consola.MenuPrincipal;
import clinicasonrisafeliz.presentacion.consola.MenuTurnos;

/**
 * Punto de entrada de la versión por consola (entregas 1 a 3).
 *
 * Se conserva como alternativa a la interfaz gráfica ({@link Main}) para poder
 * comparar ambas presentaciones sobre la misma capa de servicios y persistencia.
 * Reutiliza {@link ContextoAplicacion} para el armado de capas y la carga de datos.
 */
public class MainConsola {

    public static void main(String[] args) {
        ContextoAplicacion contexto = new ContextoAplicacion();

        // Red de seguridad: confirma el cierre limpio (los repos ya persisten en cada cambio).
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("\n✓ Clínica Sonrisa Feliz cerrada correctamente.")));

        ConsolaUtils  utils    = new ConsolaUtils(new Scanner(System.in));
        MenuLogin     login    = new MenuLogin(contexto.getControladorRecepcionista(), utils);
        Recepcionista operador = login.iniciar();

        MenuPacientes   menuPacientes   = new MenuPacientes(contexto.getControladorPaciente(), utils);
        MenuOdontologos menuOdontologos = new MenuOdontologos(contexto.getControladorOdontologo(), utils);
        MenuTurnos      menuTurnos      = new MenuTurnos(contexto.getControladorTurno(),
                                                         menuPacientes, menuOdontologos, utils, operador);
        MenuPrincipal   menu            = new MenuPrincipal(menuPacientes, menuOdontologos, menuTurnos, operador);

        menu.iniciar();
    }
}
