package clinicasonrisafeliz;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.controlador.ControladorTurno;
import clinicasonrisafeliz.io.PersistenciaCSV;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Turno;
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
import clinicasonrisafeliz.servicio.ServicioTurno;

public class Main {
    public static void main(String[] args) {
        // ── Repositorios ──────────────────────────────────────────────────────
        RepositorioPaciente   repoPaciente   = new RepositorioPaciente();
        RepositorioOdontologo repoOdontologo = new RepositorioOdontologo();
        RepositorioTurno      repoTurno      = new RepositorioTurno();

        // ── Servicios de dominio ──────────────────────────────────────────────
        ServicioPaciente   servicioPaciente   = new ServicioPaciente(repoPaciente, repoTurno);
        ServicioOdontologo servicioOdontologo = new ServicioOdontologo(repoOdontologo, repoTurno);
        ServicioTurno      servicioTurno      = new ServicioTurno(repoTurno, repoPaciente, repoOdontologo);

        // ── Carga de datos ────────────────────────────────────────────────────
        if (PersistenciaCSV.existenArchivos()) {
            try {
                cargarDesdeCSV(repoPaciente, repoOdontologo, repoTurno);
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

        // ── Shutdown hook ─────────────────────────────────────────────────────
        // Los repositorios persisten automáticamente en cada mutación.
        // Este hook es solo una red de seguridad que confirma el cierre limpio.
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
            System.out.println("\n✓ Clínica Sonrisa Feliz cerrada correctamente.")
        ));

        // ── Controladores ─────────────────────────────────────────────────────
        ControladorPaciente   controladorPaciente   = new ControladorPaciente(servicioPaciente);
        ControladorOdontologo controladorOdontologo = new ControladorOdontologo(servicioOdontologo);
        ControladorTurno      controladorTurno      = new ControladorTurno(servicioTurno);

        // ── Presentación ──────────────────────────────────────────────────────
        ConsolaUtils    utils           = new ConsolaUtils(new Scanner(System.in));
        MenuPacientes   menuPacientes   = new MenuPacientes(controladorPaciente, utils);
        MenuOdontologos menuOdontologos = new MenuOdontologos(controladorOdontologo, utils);
        MenuTurnos      menuTurnos      = new MenuTurnos(controladorTurno, menuPacientes, menuOdontologos, utils);
        MenuPrincipal   menu            = new MenuPrincipal(menuPacientes, menuOdontologos, menuTurnos);

        menu.iniciar();
    }

    private static void cargarDesdeCSV(RepositorioPaciente repoPaciente, RepositorioOdontologo repoOdontologo, RepositorioTurno repoTurno) throws IOException {
        List<Paciente>   pacientes   = PersistenciaCSV.cargarPacientes();
        List<Odontologo> odontologos = PersistenciaCSV.cargarOdontologos();
        List<Turno>      turnos      = PersistenciaCSV.cargarTurnos(pacientes, odontologos);
        PersistenciaCSV.resetContadores(pacientes, odontologos, turnos);
        repoPaciente.inicializar(pacientes);
        repoOdontologo.inicializar(odontologos);
        repoTurno.inicializar(turnos);
    }

    private static void cargarDatosDePrueba(ControladorPaciente controladorPaciente,
                                            ControladorOdontologo controladorOdontologo) {
        controladorPaciente.registrar("Juan",   "García",    "juan.garcia@email.com",        "30123456", new Domicilio("Av. Corrientes", "1234", "Buenos Aires", "CABA"));
        controladorPaciente.registrar("María",  "López",     "maria.lopez@email.com",         "28456789", new Domicilio("Calle Florida",   "567",  "Rosario",       "Santa Fe"));
        controladorPaciente.registrar("Carlos", "Martínez",  "carlos.martinez@email.com",     "35789012", new Domicilio("Belgrano",         "890",  "Córdoba",       "Córdoba"));
        controladorPaciente.registrar("Ana",    "Fernández", "ana.fernandez@email.com",        "32345678", new Domicilio("San Martín",       "321",  "Mendoza",       "Mendoza"));

        controladorOdontologo.registrar("Laura",   "Rodríguez", "laura.rodriguez@clinica.com", "MAT-1001");
        controladorOdontologo.registrar("Diego",   "Sánchez",   "diego.sanchez@clinica.com",   "MAT-1002");
        controladorOdontologo.registrar("Valeria", "Torres",    "valeria.torres@clinica.com",  "MAT-1003");
    }
}
