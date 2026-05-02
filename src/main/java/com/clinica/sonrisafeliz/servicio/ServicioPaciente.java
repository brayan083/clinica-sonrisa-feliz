package com.clinica.sonrisafeliz.servicio;

import com.clinica.sonrisafeliz.excepcion.DniDuplicadoException;
import com.clinica.sonrisafeliz.excepcion.PacienteNoEncontradoException;
import com.clinica.sonrisafeliz.modelo.Domicilio;
import com.clinica.sonrisafeliz.modelo.Paciente;
import com.clinica.sonrisafeliz.modelo.Turno;
import com.clinica.sonrisafeliz.repositorio.RepositorioPaciente;
import com.clinica.sonrisafeliz.repositorio.RepositorioTurno;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioPaciente {

    private final RepositorioPaciente repositorioPaciente;
    private final RepositorioTurno repositorioTurno;

    public ServicioPaciente(RepositorioPaciente repositorioPaciente, RepositorioTurno repositorioTurno) {
        this.repositorioPaciente = repositorioPaciente;
        this.repositorioTurno = repositorioTurno;
    }

    public Paciente registrar(String nombre, String apellido, String email, String dni, Domicilio domicilio) {
        if (repositorioPaciente.existeDni(dni)) {
            throw new DniDuplicadoException("Ya existe un paciente con el DNI: " + dni);
        }
        Paciente paciente = new Paciente(nombre, apellido, email, dni, domicilio);
        repositorioPaciente.guardar(paciente);
        return paciente;
    }

    public Paciente buscarPorId(Long id) {
        return repositorioPaciente.buscarPorId(id)
                .orElseThrow(() -> new PacienteNoEncontradoException("No se encontró paciente con ID: " + id));
    }

    public Paciente buscarPorDni(String dni) {
        return repositorioPaciente.buscarPorDni(dni)
                .orElseThrow(() -> new PacienteNoEncontradoException("No se encontró paciente con DNI: " + dni));
    }

    public List<Paciente> listarTodos() {
        return repositorioPaciente.buscarTodos().stream()
                .sorted(Comparator.comparing(Paciente::getApellido).thenComparing(Paciente::getNombre))
                .collect(Collectors.toList());
    }

    public void actualizar(Paciente paciente) {
        buscarPorId(paciente.getId());
        repositorioPaciente.actualizar(paciente);
    }

    public void eliminar(Long id) {
        Paciente paciente = buscarPorId(id);
        boolean tieneTurnosFuturos = repositorioTurno.buscarPorPacienteId(id).stream()
                .anyMatch(Turno::esFuturo);
        if (tieneTurnosFuturos) {
            throw new IllegalStateException("No se puede eliminar: el paciente " +
                    paciente.getNombreCompleto() + " tiene turnos futuros asignados.");
        }
        repositorioPaciente.eliminar(id);
    }
}
