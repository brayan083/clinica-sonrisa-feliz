package clinicasonrisafeliz.repositorio;

import clinicasonrisafeliz.modelo.Paciente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositorioPaciente implements IRepositorio<Paciente> {

    private final Map<Long, Paciente> almacenamiento = new HashMap<>();

    @Override
    public void guardar(Paciente paciente) {
        almacenamiento.put(paciente.getId(), paciente);
    }

    @Override
    public Optional<Paciente> buscarPorId(Long id) {
        return Optional.ofNullable(almacenamiento.get(id));
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

    public Optional<Paciente> buscarPorDni(String dni) {
        return almacenamiento.values().stream()
                .filter(p -> p.getDni().equals(dni))
                .findFirst();
    }

    public boolean existeDni(String dni) {
        return buscarPorDni(dni).isPresent();
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
