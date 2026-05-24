package clinicasonrisafeliz.repositorio;

import clinicasonrisafeliz.enums.EstadoTurno;
import clinicasonrisafeliz.modelo.Turno;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTurno implements IRepositorio<Turno> {

    private final List<Turno> almacenamiento = new ArrayList<>();

    @Override
    public void guardar(Turno turno) {
        almacenamiento.add(turno);
    }

    @Override
    public Turno buscarPorId(Long id) {
        for (Turno t : almacenamiento) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null; // devuelve null si no existe
    }

    @Override
    public List<Turno> buscarTodos() {
        return new ArrayList<>(almacenamiento);
    }

    @Override
    public void actualizar(Turno turno) {
        // El turno ya es el mismo objeto en memoria; no se necesita reemplazar.
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
}
