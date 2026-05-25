package clinicasonrisafeliz.servicio;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import clinicasonrisafeliz.excepcion.MatriculaDuplicadaException;
import clinicasonrisafeliz.excepcion.OdontologoNoEncontradoException;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.repositorio.RepositorioOdontologo;

public class ServicioOdontologo {

    private final RepositorioOdontologo repositorioOdontologo;

    public ServicioOdontologo(RepositorioOdontologo repositorioOdontologo) {
        this.repositorioOdontologo = repositorioOdontologo;
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

    public List<Odontologo> listarTodos() {
        return repositorioOdontologo.buscarTodos()
                .stream()
                .sorted(Comparator.comparing(Odontologo::getApellido)
                                  .thenComparing(Odontologo::getNombre))
                .collect(Collectors.toList());
    }

    public void actualizar(Long id, String nombre, String apellido, String email) {
        Odontologo odontologo = buscarPorId(id);
        odontologo.setNombre(nombre);
        odontologo.setApellido(apellido);
        odontologo.setEmail(email);
        repositorioOdontologo.actualizar(odontologo);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        repositorioOdontologo.eliminar(id);
    }
}
