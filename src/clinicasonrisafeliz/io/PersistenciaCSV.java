package clinicasonrisafeliz.io;

import clinicasonrisafeliz.enums.EstadoTurno;
import clinicasonrisafeliz.modelo.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Utilidad de persistencia CSV para Pacientes, Odontólogos y Turnos.
 * Usa BufferedReader/BufferedWriter para lectura y escritura eficiente.
 * Separador: ";" para evitar conflictos con comas en los datos.
 */
public class PersistenciaCSV {

    private static final String DIR           = "datos";
    private static final String PACIENTES_CSV  = DIR + "/pacientes.csv";
    private static final String ODONTOLOGOS_CSV = DIR + "/odontologos.csv";
    private static final String TURNOS_CSV     = DIR + "/turnos.csv";
    private static final String SEP            = ";";

    public static boolean existenArchivos() {
        return new File(PACIENTES_CSV).exists()
            && new File(ODONTOLOGOS_CSV).exists()
            && new File(TURNOS_CSV).exists();
    }

    // ─── GUARDAR ────────────────────────────────────────────────────────────

    public static void guardarPacientes(List<Paciente> pacientes) throws IOException {
        new File(DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PACIENTES_CSV))) {
            bw.write("id;nombre;apellido;email;dni;fechaIngreso;domId;calle;numero;localidad;provincia");
            bw.newLine();
            for (Paciente p : pacientes) {
                Domicilio d = p.getDomicilio();
                bw.write(p.getId() + SEP + p.getNombre() + SEP + p.getApellido() + SEP
                       + p.getEmail() + SEP + p.getDni() + SEP + p.getFechaIngreso() + SEP
                       + d.getId() + SEP + d.getCalle() + SEP + d.getNumero() + SEP
                       + d.getLocalidad() + SEP + d.getProvincia());
                bw.newLine();
            }
        }
    }

    public static void guardarOdontologos(List<Odontologo> odontologos) throws IOException {
        new File(DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ODONTOLOGOS_CSV))) {
            bw.write("id;nombre;apellido;email;matricula");
            bw.newLine();
            for (Odontologo o : odontologos) {
                bw.write(o.getId() + SEP + o.getNombre() + SEP + o.getApellido() + SEP
                       + o.getEmail() + SEP + o.getMatricula());
                bw.newLine();
            }
        }
    }

    public static void guardarTurnos(List<Turno> turnos) throws IOException {
        new File(DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TURNOS_CSV))) {
            bw.write("id;pacienteId;odontologoId;fecha;hora;estado");
            bw.newLine();
            for (Turno t : turnos) {
                bw.write(t.getId() + SEP + t.getPaciente().getId() + SEP
                       + t.getOdontologo().getId() + SEP + t.getFecha() + SEP
                       + t.getHora() + SEP + t.getEstado());
                bw.newLine();
            }
        }
    }

    // ─── CARGAR ─────────────────────────────────────────────────────────────

    public static List<Paciente> cargarPacientes() throws IOException {
        List<Paciente> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PACIENTES_CSV))) {
            br.readLine(); // saltar encabezado
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] c = linea.split(SEP, -1);
                long   id          = Long.parseLong(c[0]);
                String nombre      = c[1];
                String apellido    = c[2];
                String email       = c[3];
                String dni         = c[4];
                LocalDate fechaIng = LocalDate.parse(c[5]);
                long   domId       = Long.parseLong(c[6]);
                Domicilio dom = new Domicilio(domId, c[7], c[8], c[9], c[10]);
                lista.add(new Paciente(id, nombre, apellido, email, dni, fechaIng, dom));
            }
        }
        return lista;
    }

    public static List<Odontologo> cargarOdontologos() throws IOException {
        List<Odontologo> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ODONTOLOGOS_CSV))) {
            br.readLine(); // saltar encabezado
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] c = linea.split(SEP, -1);
                lista.add(new Odontologo(Long.parseLong(c[0]), c[1], c[2], c[3], c[4]));
            }
        }
        return lista;
    }

    /**
     * Carga los turnos relacionando cada uno con su Paciente y Odontólogo por ID.
     * Usa Iterator explícito para construir el mapa de odontólogos (demostración de colecciones).
     */
    public static List<Turno> cargarTurnos(List<Paciente> pacientes,
                                            List<Odontologo> odontologos) throws IOException {
        Map<Long, Paciente> mapaPacientes = new HashMap<>();
        for (Paciente p : pacientes) {
            mapaPacientes.put(p.getId(), p);
        }

        Map<Long, Odontologo> mapaOdontologos = new HashMap<>();
        Iterator<Odontologo> iter = odontologos.iterator();
        while (iter.hasNext()) {
            Odontologo o = iter.next();
            mapaOdontologos.put(o.getId(), o);
        }

        List<Turno> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TURNOS_CSV))) {
            br.readLine(); // saltar encabezado
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] c        = linea.split(SEP, -1);
                long id           = Long.parseLong(c[0]);
                Paciente paciente = mapaPacientes.get(Long.parseLong(c[1]));
                Odontologo odon   = mapaOdontologos.get(Long.parseLong(c[2]));
                if (paciente == null || odon == null) continue;
                LocalDate  fecha  = LocalDate.parse(c[3]);
                LocalTime  hora   = LocalTime.parse(c[4]);
                EstadoTurno estado = EstadoTurno.valueOf(c[5]);
                Turno t = new Turno(id, paciente, odon, fecha, hora, estado);
                paciente.agregarTurno(t);
                odon.getAgenda().agregarTurno(t);
                lista.add(t);
            }
        }
        return lista;
    }

    // ─── RESET DE CONTADORES ─────────────────────────────────────────────────

    /**
     * Restablece los contadores estáticos de ID para que nuevas entidades
     * no colisionen con las cargadas desde CSV.
     */
    public static void resetContadores(List<Paciente> pacientes,
                                        List<Odontologo> odontologos,
                                        List<Turno> turnos) {
        long maxPersona = 0;
        for (Paciente p : pacientes) {
            if (p.getId() > maxPersona) maxPersona = p.getId();
        }
        for (Odontologo o : odontologos) {
            if (o.getId() > maxPersona) maxPersona = o.getId();
        }
        Persona.resetContador(maxPersona + 1);

        long maxDomicilio = 0;
        for (Paciente p : pacientes) {
            if (p.getDomicilio() != null && p.getDomicilio().getId() > maxDomicilio) {
                maxDomicilio = p.getDomicilio().getId();
            }
        }
        Domicilio.resetContador(maxDomicilio + 1);

        long maxTurno = 0;
        for (Turno t : turnos) {
            if (t.getId() > maxTurno) maxTurno = t.getId();
        }
        Turno.resetContador(maxTurno + 1);
    }
}
