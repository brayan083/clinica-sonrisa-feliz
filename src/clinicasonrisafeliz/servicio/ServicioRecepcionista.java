package clinicasonrisafeliz.servicio;

import clinicasonrisafeliz.excepcion.DatoInvalidoException;
import clinicasonrisafeliz.modelo.Recepcionista;
import clinicasonrisafeliz.repositorio.RepositorioRecepcionista;

public class ServicioRecepcionista {

    private final RepositorioRecepcionista repositorio;

    public ServicioRecepcionista(RepositorioRecepcionista repositorio) {
        this.repositorio = repositorio;
    }

    public Recepcionista registrar(String nombre, String apellido, String email, String legajo) {
        if (repositorio.buscarPorLegajo(legajo) != null) {
            throw new DatoInvalidoException("Ya existe un recepcionista con el legajo: " + legajo);
        }
        Recepcionista r = new Recepcionista(nombre, apellido, email, legajo);
        repositorio.guardar(r);
        return r;
    }

    public Recepcionista buscarPorLegajo(String legajo) {
        return repositorio.buscarPorLegajo(legajo);
    }

    public boolean hayRecepcionistas() {
        return repositorio.tieneRecepcionistas();
    }
}
