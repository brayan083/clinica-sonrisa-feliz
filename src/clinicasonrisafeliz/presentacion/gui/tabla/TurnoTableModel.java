package clinicasonrisafeliz.presentacion.gui.tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.presentacion.gui.GuiUtils;

/**
 * Modelo de tabla para mostrar turnos en un {@link javax.swing.JTable}.
 * Resuelve en cada celda los datos relacionados (paciente, odontólogo,
 * recepcionista) a partir del objeto {@link Turno}.
 */
public class TurnoTableModel extends AbstractTableModel {

    private static final String[] COLUMNAS =
            {"ID", "Fecha", "Hora", "Paciente", "Odontólogo", "Estado", "Recepcionista"};

    private List<Turno> turnos = new ArrayList<>();

    public void setTurnos(List<Turno> turnos) {
        this.turnos = (turnos != null) ? turnos : new ArrayList<>();
        fireTableDataChanged();
    }

    public Turno getTurnoEn(int fila) {
        if (fila < 0 || fila >= turnos.size()) return null;
        return turnos.get(fila);
    }

    @Override
    public int getRowCount() { return turnos.size(); }

    @Override
    public int getColumnCount() { return COLUMNAS.length; }

    @Override
    public String getColumnName(int columna) { return COLUMNAS[columna]; }

    @Override
    public boolean isCellEditable(int fila, int columna) { return false; }

    @Override
    public Object getValueAt(int fila, int columna) {
        Turno t = turnos.get(fila);
        return switch (columna) {
            case 0 -> t.getId();
            case 1 -> t.getFecha().format(GuiUtils.FMT_FECHA);
            case 2 -> t.getHora().format(GuiUtils.FMT_HORA);
            case 3 -> t.getPaciente().getNombreCompleto();
            case 4 -> t.getOdontologo().getNombreCompleto();
            case 5 -> t.getEstado();
            case 6 -> t.getRecepcionista().getNombreCompleto();
            default -> "";
        };
    }
}
