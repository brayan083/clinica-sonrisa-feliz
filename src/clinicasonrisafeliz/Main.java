package clinicasonrisafeliz;

import java.util.Scanner;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.controlador.ControladorTurno;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.presentacion.consola.ConsolaUtils;
import clinicasonrisafeliz.presentacion.consola.MenuOdontologos;
import clinicasonrisafeliz.presentacion.consola.MenuPacientes;
import clinicasonrisafeliz.presentacion.consola.MenuPrincipal;
import clinicasonrisafeliz.presentacion.consola.MenuTurnos;
import clinicasonrisafeliz.repositorio.RepositorioOdontologo;
import clinicasonrisafeliz.repositorio.RepositorioPaciente;
import clinicasonrisafeliz.repositorio.RepositorioTurno;
import clinicasonrisafeliz.servicio.ServicioOdontologo;
import clinicasonrisafeliz.servicio.ServicioPaciente;
import clinicasonrisafeliz.servicio.ServicioPersistencia;
import clinicasonrisafeliz.servicio.ServicioTurno;

public class Main {
    public static void main(String[] args) {
        // ── Repositorios ──────────────────────────────────────────────────────
        RepositorioPaciente   repoPaciente   = new RepositorioPaciente();
        RepositorioOdontologo repoOdontologo = new RepositorioOdontologo();
        RepositorioTurno      repoTurno      = new RepositorioTurno();

        // ── Servicios ─────────────────────────────────────────────────────────
        ServicioPaciente   servicioPaciente   = new ServicioPaciente(repoPaciente, repoTurno);
        ServicioOdontologo servicioOdontologo = new ServicioOdontologo(repoOdontologo, repoTurno);
        ServicioTurno      servicioTurno      = new ServicioTurno(repoTurno, servicioPaciente, servicioOdontologo);
        ServicioPersistencia servicioPersistencia = new ServicioPersistencia(repoPaciente, repoOdontologo, repoTurno);

        // ── Carga de datos ────────────────────────────────────────────────────
        if (servicioPersistencia.existenDatosGuardados()) {
            try {
                servicioPersistencia.cargar();
                System.out.println("✓ Datos cargados desde archivos CSV.");
            } catch (Exception e) {
                System.out.println("⚠ Error al cargar datos: " + e.getMessage());
                System.out.println("  Se inicia con datos de prueba.");
                cargarDatosDePrueba(new ControladorPaciente(servicioPaciente),
                                    new ControladorOdontologo(servicioOdontologo));
            }
        } else {
            System.out.println("No se encontraron datos guardados. Cargando datos de prueba...");
            cargarDatosDePrueba(new ControladorPaciente(servicioPaciente),
                                new ControladorOdontologo(servicioOdontologo));
        }

        // ── Shutdown hook: guardar al cerrar ──────────────────────────────────
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                servicioPersistencia.guardar();
                System.out.println("\n✓ Datos guardados automáticamente al cerrar.");
            } catch (Exception e) {
                System.out.println("\n⚠ Error al guardar datos: " + e.getMessage());
            }
        }));

        // ── Controladores ─────────────────────────────────────────────────────
        ControladorPaciente   controladorPaciente   = new ControladorPaciente(servicioPaciente);
        ControladorOdontologo controladorOdontologo = new ControladorOdontologo(servicioOdontologo);
        ControladorTurno      controladorTurno      = new ControladorTurno(servicioTurno);

        // ── Presentación ──────────────────────────────────────────────────────
        ConsolaUtils utils = new ConsolaUtils(new Scanner(System.in));
        MenuPacientes   menuPacientes   = new MenuPacientes(controladorPaciente, utils);
        MenuOdontologos menuOdontologos = new MenuOdontologos(controladorOdontologo, utils);
        MenuTurnos      menuTurnos      = new MenuTurnos(controladorTurno, menuPacientes, menuOdontologos, utils);
        MenuPrincipal   menu            = new MenuPrincipal(menuPacientes, menuOdontologos, menuTurnos, servicioPersistencia);

        menu.iniciar();
    }

    private static void cargarDatosDePrueba(ControladorPaciente controladorPaciente, ControladorOdontologo controladorOdontologo) {
        controladorPaciente.registrar("Juan", "García", "juan.garcia@email.com", "30123456", new Domicilio("Av. Corrientes", "1234", "Buenos Aires", "CABA"));
        controladorPaciente.registrar("María", "López", "maria.lopez@email.com", "28456789", new Domicilio("Calle Florida", "567", "Rosario", "Santa Fe"));
        controladorPaciente.registrar("Carlos", "Martínez", "carlos.martinez@email.com", "35789012", new Domicilio("Belgrano", "890", "Córdoba", "Córdoba"));
        controladorPaciente.registrar("Ana", "Fernández", "ana.fernandez@email.com", "32345678", new Domicilio("San Martín", "321", "Mendoza", "Mendoza"));

        controladorOdontologo.registrar("Laura", "Rodríguez", "laura.rodriguez@clinica.com", "MAT-1001");
        controladorOdontologo.registrar("Diego", "Sánchez", "diego.sanchez@clinica.com", "MAT-1002");
        controladorOdontologo.registrar("Valeria", "Torres", "valeria.torres@clinica.com", "MAT-1003");
    }
}
