package clinicasonrisafeliz;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.controlador.ControladorRecepcionista;
import clinicasonrisafeliz.controlador.ControladorTurno;
import clinicasonrisafeliz.io.PersistenciaCSV;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Recepcionista;
import clinicasonrisafeliz.modelo.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import clinicasonrisafeliz.presentacion.gui.VentanaLogin;
import clinicasonrisafeliz.presentacion.gui.VentanaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.awt.Color;
import clinicasonrisafeliz.repositorio.RepositorioOdontologo;
import clinicasonrisafeliz.repositorio.RepositorioPaciente;
import clinicasonrisafeliz.repositorio.RepositorioRecepcionista;
import clinicasonrisafeliz.repositorio.RepositorioTurno;
import clinicasonrisafeliz.servicio.ServicioOdontologo;
import clinicasonrisafeliz.servicio.ServicioPaciente;
import clinicasonrisafeliz.servicio.ServicioRecepcionista;
import clinicasonrisafeliz.servicio.ServicioTurno;

public class Main {
    public static void main(String[] args) {
        // ── Repositorios ──────────────────────────────────────────────────────
        RepositorioPaciente      repoPaciente      = new RepositorioPaciente();
        RepositorioOdontologo    repoOdontologo    = new RepositorioOdontologo();
        RepositorioTurno         repoTurno         = new RepositorioTurno();
        RepositorioRecepcionista repoRecepcionista = new RepositorioRecepcionista();

        // ── Servicios de dominio ──────────────────────────────────────────────
        ServicioPaciente      servicioPaciente      = new ServicioPaciente(repoPaciente);
        ServicioOdontologo    servicioOdontologo    = new ServicioOdontologo(repoOdontologo);
        ServicioTurno         servicioTurno         = new ServicioTurno(repoTurno);
        ServicioRecepcionista servicioRecepcionista = new ServicioRecepcionista(repoRecepcionista);

        // ── Carga de datos ────────────────────────────────────────────────────
        // Las recepcionistas se cargan primero porque los turnos las referencian
        if (PersistenciaCSV.existenRecepcionistas()) {
            try {
                List<Recepcionista> recepcionistas = PersistenciaCSV.cargarRecepcionistas();
                repoRecepcionista.inicializar(recepcionistas);
            } catch (Exception e) {
                System.out.println("⚠ Error al cargar recepcionistas: " + e.getMessage());
            }
        }

        if (PersistenciaCSV.existenArchivos()) {
            try {
                cargarDesdeCSV(repoPaciente, repoOdontologo, repoTurno, repoRecepcionista.buscarTodos());
                System.out.println("✓ Datos cargados desde archivos CSV.");
            } catch (Exception e) {
                System.out.println("⚠ Error al cargar datos: " + e.getMessage());
                System.out.println("  Se inicia con datos de prueba.");
                cargarDatosDePrueba(
                    new ControladorRecepcionista(servicioRecepcionista),
                    new ControladorPaciente(servicioPaciente, servicioTurno),
                    new ControladorOdontologo(servicioOdontologo, servicioTurno),
                    new ControladorTurno(servicioTurno, servicioPaciente, servicioOdontologo));
            }
        } else {
            System.out.println("No se encontraron datos guardados. Cargando datos de prueba...");
            cargarDatosDePrueba(
                new ControladorRecepcionista(servicioRecepcionista),
                new ControladorPaciente(servicioPaciente, servicioTurno),
                new ControladorOdontologo(servicioOdontologo, servicioTurno),
                new ControladorTurno(servicioTurno, servicioPaciente, servicioOdontologo));
        }

        // ── Shutdown hook ─────────────────────────────────────────────────────
        // Los repositorios persisten automáticamente en cada mutación.
        // Este hook es solo una red de seguridad que confirma el cierre limpio.
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
            System.out.println("\n✓ Clínica Sonrisa Feliz cerrada correctamente.")
        ));

        // ── Controladores ─────────────────────────────────────────────────────
        ControladorPaciente      controladorPaciente      = new ControladorPaciente(servicioPaciente, servicioTurno);
        ControladorOdontologo    controladorOdontologo    = new ControladorOdontologo(servicioOdontologo, servicioTurno);
        ControladorTurno         controladorTurno         = new ControladorTurno(servicioTurno, servicioPaciente, servicioOdontologo);
        ControladorRecepcionista controladorRecepcionista = new ControladorRecepcionista(servicioRecepcionista);

        // ── Presentación GUI (Swing) ──────────────────────────────────────────
        SwingUtilities.invokeLater(() -> {
            configurarAparienciaVisual();

            VentanaLogin login = new VentanaLogin(null, controladorRecepcionista);
            login.setVisible(true);

            Recepcionista operador = login.getRecepcionistaAutenticado();
            if (operador != null) {
                VentanaPrincipal ventana = new VentanaPrincipal(
                        controladorPaciente,
                        controladorOdontologo,
                        controladorTurno,
                        controladorRecepcionista,
                        operador
                );
                ventana.setVisible(true);
            } else {
                System.out.println("Inicio de sesión cancelado o fallido. Cerrando aplicación.");
                System.exit(0);
            }
        });
    }

    private static void configurarAparienciaVisual() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar Nimbus L&F.");
        }

        // Fuente global
        Font globalFont = new Font("Roboto", Font.PLAIN, 14);
        if (!java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames().toString().contains("Roboto")) {
            globalFont = new Font("Segoe UI", Font.PLAIN, 14);
        }
        setUIFont(new FontUIResource(globalFont));

        // Colores globales (Sage Green & Charcoal)
        UIManager.put("control", new Color(244, 244, 249)); // Fondo general claro
        UIManager.put("info", new Color(244, 244, 249));
        UIManager.put("nimbusBase", new Color(82, 121, 111)); // Color base (Sage)
        UIManager.put("nimbusAlertYellow", new Color(248, 187, 86));
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusFocus", new Color(132, 169, 140)); // Focus (Light Sage)
        UIManager.put("nimbusGreen", new Color(82, 121, 111));
        UIManager.put("nimbusInfoBlue", new Color(82, 121, 111));
        UIManager.put("nimbusLightBackground", new Color(255, 255, 255));
        UIManager.put("nimbusOrange", new Color(191, 98, 4));
        UIManager.put("nimbusRed", new Color(169, 46, 34));
        UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
        UIManager.put("nimbusSelectionBackground", new Color(82, 121, 111));
        UIManager.put("text", new Color(53, 79, 82)); // Charcoal text

        // Configuraciones globales para Tablas
        UIManager.put("Table.rowHeight", 25);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.gridColor", new Color(220, 220, 220));
        UIManager.put("TableHeader.background", new Color(82, 121, 111));
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("TableHeader.font", new Font("Roboto", Font.BOLD, 14));
    }

    private static void setUIFont(FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    private static void cargarDesdeCSV(RepositorioPaciente repoPaciente, RepositorioOdontologo repoOdontologo,
                                        RepositorioTurno repoTurno, List<Recepcionista> recepcionistas) throws IOException {
        List<Paciente>   pacientes   = PersistenciaCSV.cargarPacientes();
        List<Odontologo> odontologos = PersistenciaCSV.cargarOdontologos();
        List<Turno>      turnos      = PersistenciaCSV.cargarTurnos(pacientes, odontologos, recepcionistas);
        repoPaciente.inicializar(pacientes);
        repoOdontologo.inicializar(odontologos);
        repoTurno.inicializar(turnos);
    }

    private static void cargarDatosDePrueba(ControladorRecepcionista controladorRecepcionista,
                                            ControladorPaciente controladorPaciente,
                                            ControladorOdontologo controladorOdontologo,
                                            ControladorTurno controladorTurno) {
        // ── Recepcionista ─────────────────────────────────────────────────────
        Recepcionista recep = controladorRecepcionista.registrar(
                "Admin", "Sistema", "admin@clinica.com", "REC-001");

        // ── Pacientes ─────────────────────────────────────────────────────────
        Paciente juan    = controladorPaciente.registrar("Juan",   "García",    "juan.garcia@email.com",    "30123456", new Domicilio("Av. Corrientes", "1234", "Buenos Aires", "CABA"));
        Paciente maria   = controladorPaciente.registrar("María",  "López",     "maria.lopez@email.com",    "28456789", new Domicilio("Calle Florida",   "567",  "Rosario",      "Santa Fe"));
        Paciente carlos  = controladorPaciente.registrar("Carlos", "Martínez",  "carlos.martinez@email.com","35789012", new Domicilio("Belgrano",         "890",  "Córdoba",      "Córdoba"));
                          controladorPaciente.registrar("Ana",    "Fernández", "ana.fernandez@email.com",  "32345678", new Domicilio("San Martín",       "321",  "Mendoza",      "Mendoza"));

        // ── Odontólogos ───────────────────────────────────────────────────────
        Odontologo laura   = controladorOdontologo.registrar("Laura",   "Rodríguez", "laura.rodriguez@clinica.com", "MAT-1001");
        Odontologo diego   = controladorOdontologo.registrar("Diego",   "Sánchez",   "diego.sanchez@clinica.com",   "MAT-1002");
                            controladorOdontologo.registrar("Valeria", "Torres",    "valeria.torres@clinica.com",  "MAT-1003");

        // ── Turnos de muestra (fechas futuras) ────────────────────────────────
        LocalDate base = LocalDate.now();
        controladorTurno.reservar(juan.getId(),   laura.getId(), base.plusDays(3),  LocalTime.of(9,  0), recep);
        controladorTurno.reservar(maria.getId(),  diego.getId(), base.plusDays(5),  LocalTime.of(10, 30), recep);
        controladorTurno.reservar(carlos.getId(), laura.getId(), base.plusDays(7),  LocalTime.of(14, 0), recep);

        System.out.println("  → Recepcionista de prueba: legajo REC-001");
    }
}
