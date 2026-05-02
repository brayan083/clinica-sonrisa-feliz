package com.clinica.sonrisafeliz.servicio;

import com.clinica.sonrisafeliz.excepcion.MatriculaDuplicadaException;
import com.clinica.sonrisafeliz.excepcion.OdontologoNoEncontradoException;
import com.clinica.sonrisafeliz.modelo.Odontologo;
import com.clinica.sonrisafeliz.modelo.Turno;
import com.clinica.sonrisafeliz.repositorio.RepositorioOdontologo;
import com.clinica.sonrisafeliz.repositorio.RepositorioTurno;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioOdontologo {

    private final RepositorioOdontologo repositorioOdontologo;
    private final RepositorioTurno repositorioTurno;

    public ServicioOdontologo(RepositorioOdontologo repositorioOdontologo, RepositorioTurno repositorioTurno) {
        this.repositorioOdontologo = repositorioOdontologo;
        this.repositorioTurno = repositorioTurno;
    }

    public Odontologo registrar(String nombre, String apellido, String email, String matricula) {
        if (repositorioOdontologo.existeMatricula(matricula)) {
            throw new MatriculaDuplicadaException("Ya existe un odontólogo con la matrícula: " + matricula);
        }
        Odontologo odontologo = new Odontologo(nombre, apellido, email, matricula);
        repositorioOdontologo.guardar(odontologo);
        return odontologo;
    }

    public Odontologo buscarPorId(Long id) {
        return repositorioOdontologo.buscarPorId(id)
                .orElseThrow(() -> new OdontologoNoEncontradoException("No se encontró odontólogo con ID: " + id));
    }

    public Odontologo buscarPorMatricula(String matricula) {
        return repositorioOdontologo.buscarPorMatricula(matricula)
                .orElseThrow(() -> new OdontologoNoEncontradoException("No se encontró odontólogo con matrícula: " + matricula));
    }

    public List<Odontologo> listarTodos() {
        return repositorioOdontologo.buscarTodos().stream()
                .sorted(Comparator.comparing(Odontologo::getApellido).thenComparing(Odontologo::getNombre))
                .collect(Collectors.toList());
    }

    public void actualizar(Odontologo odontologo) {
        buscarPorId(odontologo.getId());
        repositorioOdontologo.actualizar(odontologo);
    }

    public void eliminar(Long id) {
        Odontologo odontologo = buscarPorId(id);
        boolean tieneTurnosFuturos = repositorioTurno.buscarPorOdontologoId(id).stream()
                .anyMatch(Turno::esFuturo);
        if (tieneTurnosFuturos) {
            throw new IllegalStateException("No se puede eliminar: el odontólogo " +
                    odontologo.getNombreCompleto() + " tiene turnos futuros asignados.");
        }
        repositorioOdontologo.eliminar(id);
    }
}
