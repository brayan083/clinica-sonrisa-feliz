package com.clinica.sonrisafeliz.repositorio;

import com.clinica.sonrisafeliz.modelo.Odontologo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositorioOdontologo implements IRepositorio<Odontologo> {

    private final Map<Long, Odontologo> almacenamiento = new HashMap<>();

    @Override
    public void guardar(Odontologo odontologo) {
        almacenamiento.put(odontologo.getId(), odontologo);
    }

    @Override
    public Optional<Odontologo> buscarPorId(Long id) {
        return Optional.ofNullable(almacenamiento.get(id));
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

    public Optional<Odontologo> buscarPorMatricula(String matricula) {
        return almacenamiento.values().stream()
                .filter(o -> o.getMatricula().equals(matricula))
                .findFirst();
    }

    public boolean existeMatricula(String matricula) {
        return buscarPorMatricula(matricula).isPresent();
    }
}
