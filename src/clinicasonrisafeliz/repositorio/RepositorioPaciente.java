package clinicasonrisafeliz.repositorio;

import clinicasonrisafeliz.modelo.Paciente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RepositorioPaciente implements IRepositorio<Paciente> {

    private final Map<Long, Paciente> almacenamiento = new HashMap<>();

    @Override
    public void guardar(Paciente paciente) {
        almacenamiento.put(paciente.getId(), paciente);
    }

    @Override
    public Paciente buscarPorId(Long id) {
        return almacenamiento.get(id); // devuelve null si no existe
    }

    @Override
    public List<Paciente> buscarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public void actualizar(Paciente paciente) {
        almacenamiento.put(paciente.getId(), paciente);
    }

    @Override
    public void eliminar(Long id) {
        almacenamiento.remove(id);
    }

    /** Busca un paciente por DNI exacto usando Iterator. Devuelve null si no existe. */
    public Paciente buscarPorDni(String dni) {
        Iterator<Paciente> iter = almacenamiento.values().iterator();
        while (iter.hasNext()) {
            Paciente p = iter.next();
            if (p.getDni().equals(dni)) {
                return p;
            }
        }
        return null;
    }

    public boolean existeDni(String dni) {
        return buscarPorDni(dni) != null;
    }

    /** Búsqueda parcial por apellido usando Iterator explícito. */
    public List<Paciente> buscarPorApellido(String apellido) {
        List<Paciente> resultado = new ArrayList<>();
        Iterator<Paciente> iter = almacenamiento.values().iterator();
        while (iter.hasNext()) {
            Paciente p = iter.next();
            if (p.getApellido().toLowerCase().contains(apellido.toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
