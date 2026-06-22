package clinicasonrisafeliz.presentacion.gui.modelos;

import clinicasonrisafeliz.modelo.Turno;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaTurnos extends AbstractTableModel {

    private final String[] columnas = {"ID", "Fecha", "Hora", "Estado", "Paciente", "Odontólogo"};
    private List<Turno> turnos;

    public ModeloTablaTurnos() {
        this.turnos = new ArrayList<>();
    }

    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
        fireTableDataChanged();
    }

    public Turno getTurnoAt(int rowIndex) {
        return turnos.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return turnos.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Turno t = turnos.get(rowIndex);
        switch (columnIndex) {
            case 0: return t.getId();
            case 1: return t.getFecha();
            case 2: return t.getHora();
            case 3: return t.getEstado();
            case 4: return t.getPaciente().getNombreCompleto();
            case 5: return t.getOdontologo().getNombreCompleto();
            default: return null;
        }
    }
}
