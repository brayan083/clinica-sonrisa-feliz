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

    // Carpeta donde se guardan todos los archivos CSV
    private static final String DIR                = "datos";
    // Rutas completas de cada archivo
    private static final String PACIENTES_CSV      = DIR + "/pacientes.csv";
    private static final String ODONTOLOGOS_CSV    = DIR + "/odontologos.csv";
    private static final String TURNOS_CSV         = DIR + "/turnos.csv";
    private static final String RECEPCIONISTAS_CSV = DIR + "/recepcionistas.csv";
    // Separador de columnas dentro de cada fila del CSV
    private static final String SEP                = ";";

    // Devuelve true si los tres archivos principales ya existen en disco
    public static boolean existenArchivos() {
        return new File(PACIENTES_CSV).exists()
            && new File(ODONTOLOGOS_CSV).exists()
            && new File(TURNOS_CSV).exists();
    }

    // Devuelve true si el archivo de recepcionistas existe en disco
    public static boolean existenRecepcionistas() {
        return new File(RECEPCIONISTAS_CSV).exists();
    }

    // ─── GUARDAR ────────────────────────────────────────────────────────────

    public static void guardarPacientes(List<Paciente> pacientes) throws IOException {
        new File(DIR).mkdirs(); // crea la carpeta "datos" si no existe
        // Abre el archivo (lo borra y empieza vacío); se cierra solo al terminar el bloque
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PACIENTES_CSV))) {
            // Primera línea: nombres de las columnas
            bw.write("id;nombre;apellido;email;dni;fechaIngreso;domId;calle;numero;localidad;provincia");
            bw.newLine(); // salto de línea
            for (Paciente p : pacientes) {
                Domicilio d = p.getDomicilio(); // obtenemos el domicilio del paciente
                // Escribimos todos los campos separados por ";" en una sola línea
                bw.write(p.getId() + SEP + p.getNombre() + SEP + p.getApellido() + SEP
                       + p.getEmail() + SEP + p.getDni() + SEP + p.getFechaIngreso() + SEP
                       + d.getId() + SEP + d.getCalle() + SEP + d.getNumero() + SEP
                       + d.getLocalidad() + SEP + d.getProvincia());
                bw.newLine(); // salto de línea al terminar cada paciente
            }
        } catch (IOException e) {
            throw new IOException("Error al guardar pacientes en CSV: " + e.getMessage(), e);
        }
    }

    public static void guardarOdontologos(List<Odontologo> odontologos) throws IOException {
        new File(DIR).mkdirs(); // crea la carpeta "datos" si no existe
        // Abre el archivo vacío con buffer para escribir eficientemente
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ODONTOLOGOS_CSV))) {
            // Primera línea: nombres de las columnas
            bw.write("id;nombre;apellido;email;matricula");
            bw.newLine();
            for (Odontologo o : odontologos) {
                // Una línea por odontólogo con sus 5 campos
                bw.write(o.getId() + SEP + o.getNombre() + SEP + o.getApellido() + SEP
                       + o.getEmail() + SEP + o.getMatricula());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Error al guardar odontólogos en CSV: " + e.getMessage(), e);
        }
    }

    public static void guardarTurnos(List<Turno> turnos) throws IOException {
        new File(DIR).mkdirs(); // crea la carpeta "datos" si no existe
        // Abre el archivo vacío con buffer
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TURNOS_CSV))) {
            // Primera línea: nombres de las columnas
            // Nota: guardamos IDs (no el objeto completo) para no repetir datos
            bw.write("id;pacienteId;odontologoId;fecha;hora;estado;recepcionistaLegajo");
            bw.newLine();
            for (Turno t : turnos) {
                // Guardamos el ID del paciente y del odontólogo, no todos sus datos
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
        new File(DIR).mkdirs(); // crea la carpeta "datos" si no existe
        // Abre el archivo vacío con buffer
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RECEPCIONISTAS_CSV))) {
            // Primera línea: nombres de las columnas
            bw.write("id;nombre;apellido;email;legajo");
            bw.newLine();
            for (Recepcionista r : lista) {
                // Una línea por recepcionista con sus 5 campos
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
        List<Paciente> lista = new ArrayList<>(); // lista vacía donde iremos agregando pacientes
        BufferedReader br = null; // lo declaramos fuera del try para poder cerrarlo en el finally
        try {
            br = new BufferedReader(new FileReader(PACIENTES_CSV)); // abre el archivo para leer
            br.readLine(); // descarta la primera línea (encabezado con nombres de columnas)
            String linea;
            // lee línea por línea hasta llegar al final del archivo (null)
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue; // ignora líneas vacías
                String[] c = linea.split(SEP, -1); // parte la línea por ";" → array de columnas
                long      id        = Long.parseLong(c[0]); // columna 0: id
                String    nombre    = c[1];                  // columna 1: nombre
                String    apellido  = c[2];                  // columna 2: apellido
                String    email     = c[3];                  // columna 3: email
                String    dni       = c[4];                  // columna 4: dni
                LocalDate fechaIng  = LocalDate.parse(c[5]); // columna 5: fecha como LocalDate
                long      domId     = Long.parseLong(c[6]);  // columna 6: id del domicilio
                // columnas 7-10: datos del domicilio → construimos el objeto Domicilio
                Domicilio dom       = new Domicilio(domId, c[7], c[8], c[9], c[10]);
                // construimos el Paciente y lo agregamos a la lista
                lista.add(new Paciente(id, nombre, apellido, email, dni, fechaIng, dom));
            }
        } catch (IOException e) {
            throw e; // propaga al llamador para que decida cómo manejarlo
        } finally {
            // finally siempre se ejecuta, incluso si hubo excepción → garantiza el cierre
            if (br != null) {
                try { br.close(); } catch (IOException ignored) {}
            }
        }
        return lista;
    }

    public static List<Odontologo> cargarOdontologos() throws IOException {
        List<Odontologo> lista = new ArrayList<>(); // lista vacía de odontólogos
        // try-with-resources: cierra el archivo automáticamente al terminar
        try (BufferedReader br = new BufferedReader(new FileReader(ODONTOLOGOS_CSV))) {
            br.readLine(); // descarta la primera línea (encabezado)
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue; // ignora líneas vacías
                String[] c = linea.split(SEP, -1); // parte por ";" → [id, nombre, apellido, email, matricula]
                // construye el Odontologo directamente con los 5 campos y lo agrega
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
        // Convertimos la lista de pacientes a un mapa id → Paciente para búsqueda rápida
        Map<Long, Paciente> mapaPacientes = new HashMap<>();
        for (Paciente p : pacientes) {
            mapaPacientes.put(p.getId(), p); // clave: id, valor: objeto Paciente
        }

        // Igual para odontólogos, usando Iterator explícito (forma alternativa al for-each)
        Map<Long, Odontologo> mapaOdontologos = new HashMap<>();
        Iterator<Odontologo> iter = odontologos.iterator(); // crea el iterador
        while (iter.hasNext()) {                             // mientras haya siguiente elemento
            Odontologo o = iter.next();                      // obtiene el siguiente
            mapaOdontologos.put(o.getId(), o);
        }

        // Mapa de recepcionistas por legajo (String) en lugar de id numérico
        Map<String, Recepcionista> mapaRecepcionistas = new HashMap<>();
        for (Recepcionista r : recepcionistas) {
            mapaRecepcionistas.put(r.getLegajo(), r); // clave: legajo (ej: "R001")
        }

        List<Turno> lista = new ArrayList<>(); // lista donde guardaremos los turnos cargados
        try (BufferedReader br = new BufferedReader(new FileReader(TURNOS_CSV))) {
            br.readLine(); // descarta encabezado
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue; // ignora líneas vacías
                String[] c             = linea.split(SEP, -1); // parte por ";"
                long id                = Long.parseLong(c[0]);
                // buscamos el Paciente por su id en el mapa
                Paciente paciente      = mapaPacientes.get(Long.parseLong(c[1]));
                // buscamos el Odontologo por su id en el mapa
                Odontologo odon        = mapaOdontologos.get(Long.parseLong(c[2]));
                // si no existe el paciente u odontólogo, descartamos esta fila y seguimos
                if (paciente == null || odon == null) continue;
                LocalDate  fecha       = LocalDate.parse(c[3]);  // columna 3: fecha
                LocalTime  hora        = LocalTime.parse(c[4]);  // columna 4: hora
                EstadoTurno estado     = EstadoTurno.valueOf(c[5]); // columna 5: estado (PENDIENTE, etc.)
                // buscamos la recepcionista por legajo
                Recepcionista recep    = mapaRecepcionistas.get(c[6]);
                // si no existe la recepcionista, descartamos esta fila
                if (recep == null) continue;
                // construimos el turno con todos los objetos ya resueltos
                Turno t = new Turno(id, paciente, odon, fecha, hora, estado, recep);
                // agregamos el turno a la agenda del odontólogo para mantenerla sincronizada
                odon.getAgenda().agregarTurno(t);
                lista.add(t); // agregamos a la lista final
            }
        } catch (IOException e) {
            throw new IOException("Error al cargar turnos desde CSV: " + e.getMessage(), e);
        }
        return lista;
    }

    public static List<Recepcionista> cargarRecepcionistas() throws IOException {
        List<Recepcionista> lista = new ArrayList<>(); // lista vacía de recepcionistas
        // try-with-resources: cierra automáticamente al terminar
        try (BufferedReader br = new BufferedReader(new FileReader(RECEPCIONISTAS_CSV))) {
            br.readLine(); // descarta encabezado
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue; // ignora líneas vacías
                String[] c = linea.split(SEP, -1); // parte por ";" → [id, nombre, apellido, email, legajo]
                // construye la Recepcionista con los 5 campos y la agrega a la lista
                lista.add(new Recepcionista(Long.parseLong(c[0]), c[1], c[2], c[3], c[4]));
            }
        } catch (IOException e) {
            throw new IOException("Error al cargar recepcionistas desde CSV: " + e.getMessage(), e);
        }
        return lista;
    }

}
