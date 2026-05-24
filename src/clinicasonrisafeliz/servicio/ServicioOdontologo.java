package clinicasonrisafeliz.servicio;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import clinicasonrisafeliz.excepcion.MatriculaDuplicadaException;
import clinicasonrisafeliz.excepcion.OdontologoNoEncontradoException;
import clinicasonrisafeliz.excepcion.OperacionNoPermitidaException;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.repositorio.RepositorioOdontologo;
import clinicasonrisafeliz.repositorio.RepositorioTurno;

public class ServicioOdontologo {

    private final RepositorioOdontologo repositorioOdontologo;
    private final RepositorioTurno      repositorioTurno;

    public ServicioOdontologo(RepositorioOdontologo repositorioOdontologo, RepositorioTurno repositorioTurno) {
        this.repositorioOdontologo = repositorioOdontologo;
        this.repositorioTurno      = repositorioTurno;
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
        Odontologo odontologo = repositorioOdontologo.buscarPorId(id);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException("No se encontró odontólogo con ID: " + id);
        }
        return odontologo;
    }

    public Odontologo buscarPorMatricula(String matricula) {
        Odontologo odontologo = repositorioOdontologo.buscarPorMatricula(matricula);
        if (odontologo == null) {
            throw new OdontologoNoEncontradoException("No se encontró odontólogo con matrícula: " + matricula);
        }
        return odontologo;
    }

    /**
     * Lista todos los odontólogos ordenados alfabéticamente.
     * Usa Stream API: sorted(Comparator) + collect, mostrando orden con Comparator externo.
     */
    public List<Odontologo> listarTodos() {
        return repositorioOdontologo.buscarTodos()
                .stream()
                .sorted(Comparator.comparing(Odontologo::getApellido)
                                  .thenComparing(Odontologo::getNombre))
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos personales de un odontólogo a partir de sus campos individuales.
     * La mutación del objeto de dominio ocurre aquí, en la capa de servicio.
     */
    public void actualizar(Long id, String nombre, String apellido, String email) {
        Odontologo odontologo = buscarPorId(id);
        odontologo.setNombre(nombre);
        odontologo.setApellido(apellido);
        odontologo.setEmail(email);
        repositorioOdontologo.actualizar(odontologo);
    }

    public void eliminar(Long id) {
        Odontologo odontologo = buscarPorId(id);
        List<Turno> turnosOdontologo = repositorioTurno.buscarPorOdontologoId(id);
        for (Turno t : turnosOdontologo) {
            if (t.esFuturo()) {
                throw new OperacionNoPermitidaException("No se puede eliminar: el odontólogo " +
                        odontologo.getNombreCompleto() + " tiene turnos futuros asignados.");
            }
        }
        repositorioOdontologo.eliminar(id);
    }
}
