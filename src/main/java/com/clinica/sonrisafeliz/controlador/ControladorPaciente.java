package com.clinica.sonrisafeliz.controlador;

import com.clinica.sonrisafeliz.modelo.Domicilio;
import com.clinica.sonrisafeliz.modelo.Paciente;
import com.clinica.sonrisafeliz.servicio.ServicioPaciente;

import java.util.List;

public class ControladorPaciente {

    private final ServicioPaciente servicioPaciente;

    public ControladorPaciente(ServicioPaciente servicioPaciente) {
        this.servicioPaciente = servicioPaciente;
    }

    public Paciente registrar(String nombre, String apellido, String email, String dni, Domicilio domicilio) {
        return servicioPaciente.registrar(nombre, apellido, email, dni, domicilio);
    }

    public Paciente buscarPorId(Long id) {
        return servicioPaciente.buscarPorId(id);
    }

    public Paciente buscarPorDni(String dni) {
        return servicioPaciente.buscarPorDni(dni);
    }

    public List<Paciente> listarTodos() {
        return servicioPaciente.listarTodos();
    }

    public void actualizar(Paciente paciente) {
        servicioPaciente.actualizar(paciente);
    }

    public void eliminar(Long id) {
        servicioPaciente.eliminar(id);
    }
}
