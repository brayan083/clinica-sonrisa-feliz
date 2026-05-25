package clinicasonrisafeliz.repositorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clinicasonrisafeliz.io.PersistenciaCSV;
import clinicasonrisafeliz.modelo.Odontologo;

public class RepositorioOdontologo implements IRepositorio<Odontologo> {

    private final Map<Long, Odontologo> almacenamiento = new HashMap<>();

    // ── Operaciones CRUD (persisten automáticamente en CSV) ──────────────────

    @Override
    public void guardar(Odontologo odontologo) {
        almacenamiento.put(odontologo.getId(), odontologo);
        persistir();
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        return almacenamiento.get(id);
    }

    @Override
    public List<Odontologo> buscarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public void actualizar(Odontologo odontologo) {
        almacenamiento.put(odontologo.getId(), odontologo);
        persistir();
    }

    @Override
    public void eliminar(Long id) {
        almacenamiento.remove(id);
        persistir();
    }

    // ── Búsquedas ────────────────────────────────────────────────────────────

    /** Busca un odontólogo por matrícula exacta con for-each. Devuelve null si no existe. */
    public Odontologo buscarPorMatricula(String matricula) {
        for (Odontologo o : almacenamiento.values()) {
            if (o.getMatricula().equals(matricula)) {
                return o;
            }
        }
        return null;
    }

    public boolean existeMatricula(String matricula) {
        return buscarPorMatricula(matricula) != null;
    }

    // ── Carga inicial (sin persistir, evita escrituras innecesarias al arrancar) ──

    /**
     * Carga odontólogos en memoria desde una lista ya construida (ej: desde CSV).
     * No dispara escritura a disco — se usa exclusivamente durante la carga inicial.
     */
    public void inicializar(List<Odontologo> odontologos) {
        for (Odontologo o : odontologos) {
            almacenamiento.put(o.getId(), o);
        }
    }

    // ── Persistencia ─────────────────────────────────────────────────────────

    private void persistir() {
        try {
            PersistenciaCSV.guardarOdontologos(buscarTodos());
        } catch (IOException e) {
            System.err.println("⚠ Error al persistir odontólogos: " + e.getMessage());
        }
    }
}
