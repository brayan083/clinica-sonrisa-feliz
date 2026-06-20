package clinicasonrisafeliz.repositorio;

import clinicasonrisafeliz.io.PersistenciaCSV;
import clinicasonrisafeliz.modelo.Paciente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RepositorioPaciente implements IRepositorio<Paciente> {

    private final Map<Long, Paciente> almacenamiento = new HashMap<>();

    // ── Operaciones CRUD (persisten automáticamente en CSV) ──────────────────

    @Override
    public void guardar(Paciente paciente) {
        almacenamiento.put(paciente.getId(), paciente);
        persistir();
    }

    @Override
    public Paciente buscarPorId(Long id) {
        return almacenamiento.get(id);
    }

    @Override
    public List<Paciente> buscarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public void actualizar(Paciente paciente) {
        almacenamiento.put(paciente.getId(), paciente);
        persistir();
    }

    @Override
    public void eliminar(Long id) {
        almacenamiento.remove(id);
        persistir();
    }

    // ── Búsquedas ────────────────────────────────────────────────────────────

    /** Busca un paciente por DNI exacto usando Stream. Devuelve null si no existe. */
    public Paciente buscarPorDni(String dni) {
        return almacenamiento.values().stream()
                .filter(p -> p.getDni().equals(dni))
                .findFirst()
                .orElse(null);
    }

    public boolean existeDni(String dni) {
        return buscarPorDni(dni) != null;
    }

    /** Búsqueda parcial por apellido usando Stream API. */
    public List<Paciente> buscarPorApellido(String apellido) {
        return almacenamiento.values().stream()
                .filter(p -> p.getApellido().toLowerCase().contains(apellido.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }

    // ── Carga inicial (sin persistir, evita escrituras innecesarias al arrancar) ──

    /**
     * Carga pacientes en memoria desde una lista ya construida (ej: desde CSV).
     * No dispara escritura a disco — se usa exclusivamente durante la carga inicial.
     */
    public void inicializar(List<Paciente> pacientes) {
        for (Paciente p : pacientes) {
            almacenamiento.put(p.getId(), p);
        }
    }

    // ── Persistencia ─────────────────────────────────────────────────────────

    private void persistir() {
        try {
            PersistenciaCSV.guardarPacientes(buscarTodos());
        } catch (IOException e) {
            System.err.println("⚠ Error al persistir pacientes: " + e.getMessage());
        }
    }
}
