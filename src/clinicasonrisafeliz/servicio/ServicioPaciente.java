package clinicasonrisafeliz.servicio;

import clinicasonrisafeliz.excepcion.DniDuplicadoException;
import clinicasonrisafeliz.excepcion.PacienteNoEncontradoException;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.repositorio.RepositorioPaciente;
import clinicasonrisafeliz.repositorio.RepositorioTurno;

import java.util.Collections;
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
        List<Paciente> lista = repositorioPaciente.buscarTodos();
        Collections.sort(lista); // orden natural definido en Paciente.compareTo
        return lista;
    }

    /** Busca pacientes cuyo apellido contenga el texto dado (sin distinción de mayúsculas). */
    public List<Paciente> buscarPorApellido(String apellido) {
        List<Paciente> resultado = repositorioPaciente.buscarPorApellido(apellido);
        Collections.sort(resultado);
        return resultado;
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
