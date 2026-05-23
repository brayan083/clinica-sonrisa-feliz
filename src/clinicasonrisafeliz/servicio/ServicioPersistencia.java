package clinicasonrisafeliz.servicio;

import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.repositorio.PersistenciaCSV;
import clinicasonrisafeliz.repositorio.RepositorioOdontologo;
import clinicasonrisafeliz.repositorio.RepositorioPaciente;
import clinicasonrisafeliz.repositorio.RepositorioTurno;

import java.io.IOException;
import java.util.List;

/**
 * Coordina la carga y el guardado de datos entre los repositorios
 * en memoria y los archivos CSV en disco.
 */
public class ServicioPersistencia {

    private final RepositorioPaciente   repoPaciente;
    private final RepositorioOdontologo repoOdontologo;
    private final RepositorioTurno      repoTurno;

    public ServicioPersistencia(RepositorioPaciente repoPaciente,
                                RepositorioOdontologo repoOdontologo,
                                RepositorioTurno repoTurno) {
        this.repoPaciente   = repoPaciente;
        this.repoOdontologo = repoOdontologo;
        this.repoTurno      = repoTurno;
    }

    public boolean existenDatosGuardados() {
        return PersistenciaCSV.existenArchivos();
    }

    /**
     * Carga los datos desde CSV hacia los repositorios en memoria.
     * Restablece los contadores de ID para que no haya colisiones.
     */
    public void cargar() throws IOException {
        List<Paciente>   pacientes   = PersistenciaCSV.cargarPacientes();
        List<Odontologo> odontologos = PersistenciaCSV.cargarOdontologos();
        List<Turno>      turnos      = PersistenciaCSV.cargarTurnos(pacientes, odontologos);

        PersistenciaCSV.resetContadores(pacientes, odontologos, turnos);

        for (Paciente p : pacientes)     repoPaciente.guardar(p);
        for (Odontologo o : odontologos) repoOdontologo.guardar(o);
        for (Turno t : turnos)           repoTurno.guardar(t);
    }

    /** Persiste el estado actual de los repositorios en los archivos CSV. */
    public void guardar() throws IOException {
        PersistenciaCSV.guardarPacientes(repoPaciente.buscarTodos());
        PersistenciaCSV.guardarOdontologos(repoOdontologo.buscarTodos());
        PersistenciaCSV.guardarTurnos(repoTurno.buscarTodos());
    }
}
