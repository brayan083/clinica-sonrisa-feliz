package clinicasonrisafeliz.repositorio;

import clinicasonrisafeliz.modelo.Odontologo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioOdontologo implements IRepositorio<Odontologo> {

    private final Map<Long, Odontologo> almacenamiento = new HashMap<>();

    @Override
    public void guardar(Odontologo odontologo) {
        almacenamiento.put(odontologo.getId(), odontologo);
    }

    @Override
    public Odontologo buscarPorId(Long id) {
        return almacenamiento.get(id); // devuelve null si no existe
    }

    @Override
    public List<Odontologo> buscarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public void actualizar(Odontologo odontologo) {
        almacenamiento.put(odontologo.getId(), odontologo);
    }

    @Override
    public void eliminar(Long id) {
        almacenamiento.remove(id);
    }

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
}
