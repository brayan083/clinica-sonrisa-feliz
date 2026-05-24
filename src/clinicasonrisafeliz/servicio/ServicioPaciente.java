package clinicasonrisafeliz.servicio;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import clinicasonrisafeliz.excepcion.DniDuplicadoException;
import clinicasonrisafeliz.excepcion.OperacionNoPermitidaException;
import clinicasonrisafeliz.excepcion.PacienteNoEncontradoException;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.repositorio.RepositorioPaciente;
import clinicasonrisafeliz.repositorio.RepositorioTurno;

public class ServicioPaciente {

    private final RepositorioPaciente repositorioPaciente;
    private final RepositorioTurno    repositorioTurno;

    public ServicioPaciente(RepositorioPaciente repositorioPaciente, RepositorioTurno repositorioTurno) {
        this.repositorioPaciente = repositorioPaciente;
        this.repositorioTurno    = repositorioTurno;
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
     * Retorna los nombres completos de todos los pacientes en orden alfabético.
     * Usa Stream API: sorted() con Comparable + map() para transformar a String + collect().
     */
    public List<String> listarNombresCompletos() {
        return repositorioPaciente.buscarTodos()
                .stream()
                .sorted()
                .map(Paciente::getNombreCompleto)
                .collect(Collectors.toList());
    }

    /**
     * Busca pacientes cuyo apellido contenga el texto dado.
     * El repositorio realiza la búsqueda (Iterator); el servicio ordena el resultado (Stream + Comparable).
     */
    public List<Paciente> buscarPorApellido(String apellido) {
        return repositorioPaciente.buscarPorApellido(apellido)
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos personales de un paciente a partir de sus campos individuales.
     * La mutación del objeto de dominio ocurre aquí, en la capa de servicio.
     */
    public void actualizar(Long id, String nombre, String apellido, String email) {
        Paciente paciente = buscarPorId(id);
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setEmail(email);
        repositorioPaciente.actualizar(paciente);
    }

    public void eliminar(Long id) {
        Paciente paciente = buscarPorId(id);
        List<Turno> turnosPaciente = repositorioTurno.buscarPorPacienteId(id);
        for (Turno t : turnosPaciente) {
            if (t.esFuturo()) {
                throw new OperacionNoPermitidaException("No se puede eliminar: el paciente " +
                        paciente.getNombreCompleto() + " tiene turnos futuros asignados.");
            }
        }
        repositorioPaciente.eliminar(id);
    }
}
