package clinicasonrisafeliz.controlador;

import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.servicio.ServicioPaciente;

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

    public List<Paciente> buscarPorApellido(String apellido) {
        return servicioPaciente.buscarPorApellido(apellido);
    }
}
