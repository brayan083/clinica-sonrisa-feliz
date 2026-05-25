package clinicasonrisafeliz.repositorio;

import clinicasonrisafeliz.io.PersistenciaCSV;
import clinicasonrisafeliz.modelo.Recepcionista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioRecepcionista implements IRepositorio<Recepcionista> {

    private final Map<Long, Recepcionista> almacenamiento = new HashMap<>();

    @Override
    public void guardar(Recepcionista recepcionista) {
        almacenamiento.put(recepcionista.getId(), recepcionista);
        persistir();
    }

    @Override
    public Recepcionista buscarPorId(Long id) {
        return almacenamiento.get(id);
    }

    @Override
    public List<Recepcionista> buscarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public void actualizar(Recepcionista recepcionista) {
        almacenamiento.put(recepcionista.getId(), recepcionista);
        persistir();
    }

    @Override
    public void eliminar(Long id) {
        almacenamiento.remove(id);
        persistir();
    }

    public Recepcionista buscarPorLegajo(String legajo) {
        for (Recepcionista r : almacenamiento.values()) {
            if (r.getLegajo().equals(legajo)) return r;
        }
        return null;
    }

    public boolean tieneRecepcionistas() {
        return !almacenamiento.isEmpty();
    }

    public void inicializar(List<Recepcionista> lista) {
        for (Recepcionista r : lista) {
            almacenamiento.put(r.getId(), r);
        }
    }

    private void persistir() {
        try {
            PersistenciaCSV.guardarRecepcionistas(buscarTodos());
        } catch (IOException e) {
            System.err.println("⚠ Error al persistir recepcionistas: " + e.getMessage());
        }
    }
}
