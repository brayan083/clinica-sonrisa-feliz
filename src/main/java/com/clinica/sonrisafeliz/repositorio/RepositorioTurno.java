package com.clinica.sonrisafeliz.repositorio;

import com.clinica.sonrisafeliz.enums.EstadoTurno;
import com.clinica.sonrisafeliz.modelo.Turno;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositorioTurno implements IRepositorio<Turno> {

    private final List<Turno> almacenamiento = new ArrayList<>();

    @Override
    public void guardar(Turno turno) {
        almacenamiento.add(turno);
    }

    @Override
    public Optional<Turno> buscarPorId(Long id) {
        return almacenamiento.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Turno> buscarTodos() {
        return new ArrayList<>(almacenamiento);
    }

    @Override
    public void actualizar(Turno turno) {
        // El turno ya es el mismo objeto en memoria; no se necesita reemplazar.
    }

    @Override
    public void eliminar(Long id) {
        almacenamiento.removeIf(t -> t.getId().equals(id));
    }

    public List<Turno> buscarPorPacienteId(Long pacienteId) {
        return almacenamiento.stream()
                .filter(t -> t.getPaciente().getId().equals(pacienteId))
                .collect(Collectors.toList());
    }

    public List<Turno> buscarPorOdontologoId(Long odontologoId) {
        return almacenamiento.stream()
                .filter(t -> t.getOdontologo().getId().equals(odontologoId))
                .collect(Collectors.toList());
    }

    public List<Turno> buscarPorFecha(LocalDate fecha) {
        return almacenamiento.stream()
                .filter(t -> t.getFecha().equals(fecha))
                .collect(Collectors.toList());
    }

    public List<Turno> buscarPorEstado(EstadoTurno estado) {
        return almacenamiento.stream()
                .filter(t -> t.getEstado() == estado)
                .collect(Collectors.toList());
    }
}
