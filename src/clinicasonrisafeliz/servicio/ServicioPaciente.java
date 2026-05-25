package clinicasonrisafeliz.servicio;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import clinicasonrisafeliz.excepcion.DniDuplicadoException;
import clinicasonrisafeliz.excepcion.PacienteNoEncontradoException;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.repositorio.RepositorioPaciente;

public class ServicioPaciente {

    private final RepositorioPaciente repositorioPaciente;

    public ServicioPaciente(RepositorioPaciente repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
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
        Paciente paciente = repositorioPaciente.buscarPorId(id);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No se encontró paciente con ID: " + id);
        }
        return paciente;
    }

    public Paciente buscarPorDni(String dni) {
        Paciente paciente = repositorioPaciente.buscarPorDni(dni);
        if (paciente == null) {
            throw new PacienteNoEncontradoException("No se encontró paciente con DNI: " + dni);
        }
        return paciente;
    }

    public List<Paciente> listarTodos() {
        List<Paciente> lista = repositorioPaciente.buscarTodos();
        Collections.sort(lista);
        return lista;
    }

    public List<String> listarNombresCompletos() {
        return repositorioPaciente.buscarTodos()
                .stream()
                .sorted()
                .map(Paciente::getNombreCompleto)
                .collect(Collectors.toList());
    }

    public List<Paciente> buscarPorApellido(String apellido) {
        return repositorioPaciente.buscarPorApellido(apellido)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public void actualizar(Long id, String nombre, String apellido, String email) {
        Paciente paciente = buscarPorId(id);
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setEmail(email);
        repositorioPaciente.actualizar(paciente);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        repositorioPaciente.eliminar(id);
    }
}
