package clinicasonrisafeliz.servicio;

import clinicasonrisafeliz.excepcion.DniDuplicadoException;
import clinicasonrisafeliz.excepcion.PacienteNoEncontradoException;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.repositorio.RepositorioPaciente;
import clinicasonrisafeliz.repositorio.RepositorioTurno;

import java.util.Collections;
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

    /**
     * Lista todos los pacientes ordenados alfabéticamente.
     * Usa Collections.sort() con el orden natural definido en Paciente (Comparable).
     */
    public List<Paciente> listarTodos() {
        List<Paciente> lista = repositorioPaciente.buscarTodos();
        Collections.sort(lista);
        return lista;
    }

    /**
     * Busca pacientes cuyo apellido contenga el texto dado.
     * Usa Stream API: filter para filtrar + sorted para ordenar (Comparable) + collect.
     */
    public List<Paciente> buscarPorApellido(String apellido) {
        return repositorioPaciente.buscarTodos()
                .stream()
                .filter(p -> p.getApellido().toLowerCase().contains(apellido.toLowerCase()))
                .sorted()
                .collect(Collectors.toList());
    }

    public void actualizar(Paciente paciente) {
        buscarPorId(paciente.getId());
        repositorioPaciente.actualizar(paciente);
    }

    public void eliminar(Long id) {
        Paciente paciente = buscarPorId(id);
        List<Turno> turnosPaciente = repositorioTurno.buscarPorPacienteId(id);
        for (Turno t : turnosPaciente) {
            if (t.esFuturo()) {
                throw new IllegalStateException("No se puede eliminar: el paciente " +
                        paciente.getNombreCompleto() + " tiene turnos futuros asignados.");
            }
        }
        repositorioPaciente.eliminar(id);
    }
}
