package com.clinica.sonrisafeliz.controlador;

import com.clinica.sonrisafeliz.modelo.Odontologo;
import com.clinica.sonrisafeliz.servicio.ServicioOdontologo;

import java.util.List;

public class ControladorOdontologo {

    private final ServicioOdontologo servicioOdontologo;

    public ControladorOdontologo(ServicioOdontologo servicioOdontologo) {
        this.servicioOdontologo = servicioOdontologo;
    }

    public Odontologo registrar(String nombre, String apellido, String email, String matricula) {
        return servicioOdontologo.registrar(nombre, apellido, email, matricula);
    }

    public Odontologo buscarPorId(Long id) {
        return servicioOdontologo.buscarPorId(id);
    }

    public Odontologo buscarPorMatricula(String matricula) {
        return servicioOdontologo.buscarPorMatricula(matricula);
    }

    public List<Odontologo> listarTodos() {
        return servicioOdontologo.listarTodos();
    }

    public void actualizar(Odontologo odontologo) {
        servicioOdontologo.actualizar(odontologo);
    }

    public void eliminar(Long id) {
        servicioOdontologo.eliminar(id);
    }
}
