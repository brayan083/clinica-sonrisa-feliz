package clinicasonrisafeliz.repositorio;

import java.util.List;

/**
 * Interfaz genérica de repositorio con operaciones CRUD básicas.
 * buscarPorId devuelve null si no encuentra la entidad.
 */
public interface IRepositorio<T> {
    void guardar(T entidad);
    T buscarPorId(Long id);
    List<T> buscarTodos();
    void actualizar(T entidad);
    void eliminar(Long id);
}
