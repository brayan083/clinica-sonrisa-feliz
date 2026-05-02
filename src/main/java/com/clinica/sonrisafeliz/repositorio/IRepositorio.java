package com.clinica.sonrisafeliz.repositorio;

import java.util.List;
import java.util.Optional;

public interface IRepositorio<T> {
    void guardar(T entidad);
    Optional<T> buscarPorId(Long id);
    List<T> buscarTodos();
    void actualizar(T entidad);
    void eliminar(Long id);
}
