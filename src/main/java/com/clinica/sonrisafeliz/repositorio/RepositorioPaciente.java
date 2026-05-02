package com.clinica.sonrisafeliz.repositorio;

import com.clinica.sonrisafeliz.modelo.Paciente;

import java.util.ArrayList;
import java.util.HashMap;
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
}
