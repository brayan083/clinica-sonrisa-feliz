package clinicasonrisafeliz.repositorio;

import clinicasonrisafeliz.enums.EstadoTurno;
import clinicasonrisafeliz.io.PersistenciaCSV;
import clinicasonrisafeliz.modelo.Turno;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTurno implements IRepositorio<Turno> {

    private final List<Turno> almacenamiento = new ArrayList<>();

    // ── Operaciones CRUD (persisten automáticamente en CSV) ──────────────────

    @Override
    public void guardar(Turno turno) {
        almacenamiento.add(turno);
        persistir();
    }

    @Override
    public Turno buscarPorId(Long id) {
        for (Turno t : almacenamiento) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public List<Turno> buscarTodos() {
        return new ArrayList<>(almacenamiento);
    }

    /**
     * Persiste el estado actual del turno (cambios de estado, fecha u hora).
     * El turno ya existe en memoria; solo es necesario reescribir el CSV.
     */
    @Override
    public void actualizar(Turno turno) {
        persistir();
    }

    @Override
    public void eliminar(Long id) {
        Turno aEliminar = null;
        for (Turno t : almacenamiento) {
            if (t.getId().equals(id)) {
                aEliminar = t;
                break;
            }
        }
        if (aEliminar != null) {
            almacenamiento.remove(aEliminar);
            persistir();
        }
    }

    public List<Turno> buscarPorPacienteId(Long pacienteId) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : almacenamiento) {
            if (t.getPaciente().getId().equals(pacienteId)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Turno> buscarPorOdontologoId(Long odontologoId) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : almacenamiento) {
            if (t.getOdontologo().getId().equals(odontologoId)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Turno> buscarPorFecha(LocalDate fecha) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : almacenamiento) {
            if (t.getFecha().equals(fecha)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Turno> buscarPorEstado(EstadoTurno estado) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : almacenamiento) {
            if (t.getEstado() == estado) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Turno> buscarPorRangoDeFechas(LocalDate desde, LocalDate hasta) {
        List<Turno> resultado = new ArrayList<>();
        for (Turno t : almacenamiento) {
            if (!t.getFecha().isBefore(desde) && !t.getFecha().isAfter(hasta)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    // ── Carga inicial (sin persistir, evita escrituras innecesarias al arrancar) ──

    /**
     * Carga turnos en memoria desde una lista ya construida (ej: desde CSV).
     * No dispara escritura a disco — se usa exclusivamente durante la carga inicial.
     */
    public void inicializar(List<Turno> turnos) {
        almacenamiento.addAll(turnos);
    }

    // ── Persistencia ─────────────────────────────────────────────────────────

    private void persistir() {
        try {
            PersistenciaCSV.guardarTurnos(buscarTodos());
        } catch (IOException e) {
            System.err.println("⚠ Error al persistir turnos: " + e.getMessage());
        }
    }
}
