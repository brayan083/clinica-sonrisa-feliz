package clinicasonrisafeliz;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
import clinicasonrisafeliz.repositorio.RepositorioOdontologo;
import clinicasonrisafeliz.repositorio.RepositorioPaciente;
import clinicasonrisafeliz.repositorio.RepositorioRecepcionista;
import clinicasonrisafeliz.repositorio.RepositorioTurno;
import clinicasonrisafeliz.servicio.ServicioOdontologo;
import clinicasonrisafeliz.servicio.ServicioPaciente;
import clinicasonrisafeliz.servicio.ServicioRecepcionista;
import clinicasonrisafeliz.servicio.ServicioTurno;

/**
 * Punto único de armado y cableado de la aplicación (composition root).
 *
 * Construye las cuatro capas — repositorios, servicios y controladores —
 * y carga los datos iniciales (desde CSV o, si no existen, datos de prueba).
 *
 * Tanto la interfaz gráfica ({@link Main}) como la interfaz por consola
 * ({@link MainConsola}) reutilizan esta clase, de modo que la lógica de
 * inicialización vive en un solo lugar y no se duplica entre presentaciones.
 */
public class ContextoAplicacion {

    // ── Controladores expuestos a la capa de presentación ────────────────────
    private final ControladorPaciente      controladorPaciente;
    private final ControladorOdontologo    controladorOdontologo;
    private final ControladorTurno         controladorTurno;
    private final ControladorRecepcionista controladorRecepcionista;

    public ContextoAplicacion() {
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

        // ── Controladores ─────────────────────────────────────────────────────
        this.controladorPaciente      = new ControladorPaciente(servicioPaciente, servicioTurno);
        this.controladorOdontologo    = new ControladorOdontologo(servicioOdontologo, servicioTurno);
        this.controladorTurno         = new ControladorTurno(servicioTurno, servicioPaciente, servicioOdontologo);
        this.controladorRecepcionista = new ControladorRecepcionista(servicioRecepcionista);

        // ── Carga de datos ────────────────────────────────────────────────────
        // Las recepcionistas se cargan primero porque los turnos las referencian.
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
                cargarDatosDePrueba();
            }
        } else {
            System.out.println("No se encontraron datos guardados. Cargando datos de prueba...");
            cargarDatosDePrueba();
        }
    }

    // ── Acceso a controladores ────────────────────────────────────────────────

    public ControladorPaciente      getControladorPaciente()      { return controladorPaciente; }
    public ControladorOdontologo    getControladorOdontologo()    { return controladorOdontologo; }
    public ControladorTurno         getControladorTurno()         { return controladorTurno; }
    public ControladorRecepcionista getControladorRecepcionista() { return controladorRecepcionista; }

    // ── Persistencia explícita (usada al cerrar la ventana) ────────────────────

    /**
     * Fuerza el guardado de pacientes, odontólogos y turnos a disco.
     *
     * Los repositorios ya persisten automáticamente en cada mutación; este
     * método es la red de seguridad que dispara el {@code WindowListener} al
     * cerrar la aplicación, garantizando que nada quede sin escribir.
     */
    public void guardarTodo() throws IOException {
        PersistenciaCSV.guardarPacientes(controladorPaciente.listarTodos());
        PersistenciaCSV.guardarOdontologos(controladorOdontologo.listarTodos());
        PersistenciaCSV.guardarTurnos(controladorTurno.listarTodos());
    }

    // ── Helpers de inicialización ───────────────────────────────────────────────

    private void cargarDesdeCSV(RepositorioPaciente repoPaciente, RepositorioOdontologo repoOdontologo,
                                RepositorioTurno repoTurno, List<Recepcionista> recepcionistas) throws IOException {
        List<Paciente>   pacientes   = PersistenciaCSV.cargarPacientes();
        List<Odontologo> odontologos = PersistenciaCSV.cargarOdontologos();
        List<Turno>      turnos      = PersistenciaCSV.cargarTurnos(pacientes, odontologos, recepcionistas);
        repoPaciente.inicializar(pacientes);
        repoOdontologo.inicializar(odontologos);
        repoTurno.inicializar(turnos);
    }

    private void cargarDatosDePrueba() {
        // ── Recepcionista ─────────────────────────────────────────────────────
        Recepcionista recep = controladorRecepcionista.registrar(
                "Admin", "Sistema", "admin@clinica.com", "REC-001");

        // ── Pacientes ─────────────────────────────────────────────────────────
        Paciente juan   = controladorPaciente.registrar("Juan",   "García",    "juan.garcia@email.com",     "30123456", new Domicilio("Av. Corrientes", "1234", "Buenos Aires", "CABA"));
        Paciente maria  = controladorPaciente.registrar("María",  "López",     "maria.lopez@email.com",     "28456789", new Domicilio("Calle Florida",   "567",  "Rosario",      "Santa Fe"));
        Paciente carlos = controladorPaciente.registrar("Carlos", "Martínez",  "carlos.martinez@email.com", "35789012", new Domicilio("Belgrano",         "890",  "Córdoba",      "Córdoba"));
                          controladorPaciente.registrar("Ana",    "Fernández", "ana.fernandez@email.com",   "32345678", new Domicilio("San Martín",       "321",  "Mendoza",      "Mendoza"));

        // ── Odontólogos ───────────────────────────────────────────────────────
        Odontologo laura = controladorOdontologo.registrar("Laura",   "Rodríguez", "laura.rodriguez@clinica.com", "MAT-1001");
        Odontologo diego = controladorOdontologo.registrar("Diego",   "Sánchez",   "diego.sanchez@clinica.com",   "MAT-1002");
                          controladorOdontologo.registrar("Valeria", "Torres",    "valeria.torres@clinica.com",  "MAT-1003");

        // ── Turnos de muestra (fechas futuras) ────────────────────────────────
        LocalDate base = LocalDate.now();
        controladorTurno.reservar(juan.getId(),   laura.getId(), base.plusDays(3), LocalTime.of(9,  0),  recep);
        controladorTurno.reservar(maria.getId(),  diego.getId(), base.plusDays(5), LocalTime.of(10, 30), recep);
        controladorTurno.reservar(carlos.getId(), laura.getId(), base.plusDays(7), LocalTime.of(14, 0),  recep);

        System.out.println("  → Recepcionista de prueba: legajo REC-001");
    }
}
