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

    private static final String DIR                = "datos";
    private static final String PACIENTES_CSV      = DIR + "/pacientes.csv";
    private static final String ODONTOLOGOS_CSV    = DIR + "/odontologos.csv";
    private static final String TURNOS_CSV         = DIR + "/turnos.csv";
    private static final String RECEPCIONISTAS_CSV = DIR + "/recepcionistas.csv";
    private static final String SEP                = ";";

    public static boolean existenArchivos() {
        return new File(PACIENTES_CSV).exists()
            && new File(ODONTOLOGOS_CSV).exists()
            && new File(TURNOS_CSV).exists();
    }

    public static boolean existenRecepcionistas() {
        return new File(RECEPCIONISTAS_CSV).exists();
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
        } catch (IOException e) {
            throw new IOException("Error al guardar pacientes en CSV: " + e.getMessage(), e);
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
        } catch (IOException e) {
            throw new IOException("Error al guardar odontólogos en CSV: " + e.getMessage(), e);
        }
    }

    public static void guardarTurnos(List<Turno> turnos) throws IOException {
        new File(DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TURNOS_CSV))) {
            bw.write("id;pacienteId;odontologoId;fecha;hora;estado;recepcionistaLegajo");
            bw.newLine();
            for (Turno t : turnos) {
                bw.write(t.getId() + SEP + t.getPaciente().getId() + SEP
                       + t.getOdontologo().getId() + SEP + t.getFecha() + SEP
                       + t.getHora() + SEP + t.getEstado() + SEP
                       + t.getRecepcionista().getLegajo());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error al guardar turnos en CSV: " + e.getMessage(), e);
        }
    }

    public static void guardarRecepcionistas(List<Recepcionista> lista) throws IOException {
        new File(DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RECEPCIONISTAS_CSV))) {
            bw.write("id;nombre;apellido;email;legajo");
            bw.newLine();
            for (Recepcionista r : lista) {
                bw.write(r.getId() + SEP + r.getNombre() + SEP + r.getApellido() + SEP
                       + r.getEmail() + SEP + r.getLegajo());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error al guardar recepcionistas en CSV: " + e.getMessage(), e);
        }
    }

    // ─── CARGAR ─────────────────────────────────────────────────────────────

    /**
     * Carga pacientes desde CSV usando try-catch-finally para garantizar
     * el cierre del reader incluso si ocurre una excepción durante la lectura.
     */
    public static List<Paciente> cargarPacientes() throws IOException {
        List<Paciente> lista = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(PACIENTES_CSV));
            br.readLine(); // saltar encabezado
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] c = linea.split(SEP, -1);
                long      id        = Long.parseLong(c[0]);
                String    nombre    = c[1];
                String    apellido  = c[2];
                String    email     = c[3];
                String    dni       = c[4];
                LocalDate fechaIng  = LocalDate.parse(c[5]);
                long      domId     = Long.parseLong(c[6]);
                Domicilio dom       = new Domicilio(domId, c[7], c[8], c[9], c[10]);
                lista.add(new Paciente(id, nombre, apellido, email, dni, fechaIng, dom));
            }
        } catch (IOException e) {
            throw e; // propaga al llamador para que decida cómo manejarlo
        } finally {
            if (br != null) {
                try { br.close(); } catch (IOException ignored) {}
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
        } catch (IOException e) {
            throw new IOException("Error al cargar odontólogos desde CSV: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Carga los turnos relacionando cada uno con su Paciente y Odontólogo por ID.
     * Usa Iterator explícito para construir el mapa de odontólogos (demostración de colecciones).
     */
    public static List<Turno> cargarTurnos(List<Paciente> pacientes,
                                            List<Odontologo> odontologos,
                                            List<Recepcionista> recepcionistas) throws IOException {
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

        Map<String, Recepcionista> mapaRecepcionistas = new HashMap<>();
        for (Recepcionista r : recepcionistas) {
            mapaRecepcionistas.put(r.getLegajo(), r);
        }

        List<Turno> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TURNOS_CSV))) {
            br.readLine(); // saltar encabezado
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] c             = linea.split(SEP, -1);
                long id                = Long.parseLong(c[0]);
                Paciente paciente      = mapaPacientes.get(Long.parseLong(c[1]));
                Odontologo odon        = mapaOdontologos.get(Long.parseLong(c[2]));
                if (paciente == null || odon == null) continue;
                LocalDate  fecha       = LocalDate.parse(c[3]);
                LocalTime  hora        = LocalTime.parse(c[4]);
                EstadoTurno estado     = EstadoTurno.valueOf(c[5]);
                Recepcionista recep    = mapaRecepcionistas.get(c[6]);
                if (recep == null) continue;
                Turno t = new Turno(id, paciente, odon, fecha, hora, estado, recep);
                odon.getAgenda().agregarTurno(t);
                lista.add(t);
            }
        } catch (IOException e) {
            throw new IOException("Error al cargar turnos desde CSV: " + e.getMessage(), e);
        }
        return lista;
    }

    public static List<Recepcionista> cargarRecepcionistas() throws IOException {
        List<Recepcionista> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RECEPCIONISTAS_CSV))) {
            br.readLine(); // saltar encabezado
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] c = linea.split(SEP, -1);
                lista.add(new Recepcionista(Long.parseLong(c[0]), c[1], c[2], c[3], c[4]));
            }
        } catch (IOException e) {
            throw new IOException("Error al cargar recepcionistas desde CSV: " + e.getMessage(), e);
        }
        return lista;
    }

}
