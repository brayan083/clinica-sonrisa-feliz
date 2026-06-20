package clinicasonrisafeliz.presentacion.gui.modelos;

import clinicasonrisafeliz.modelo.Odontologo;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaOdontologos extends AbstractTableModel {

    private final String[] columnas = {"ID", "Nombre", "Apellido", "Matrícula", "Email"};
    private List<Odontologo> odontologos;

    public ModeloTablaOdontologos() {
        this.odontologos = new ArrayList<>();
    }

    public void setOdontologos(List<Odontologo> odontologos) {
        this.odontologos = odontologos;
        fireTableDataChanged();
    }

    public Odontologo getOdontologoAt(int rowIndex) {
        return odontologos.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return odontologos.size();
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
        Odontologo o = odontologos.get(rowIndex);
        switch (columnIndex) {
            case 0: return o.getId();
            case 1: return o.getNombre();
            case 2: return o.getApellido();
            case 3: return o.getMatricula();
            case 4: return o.getEmail();
            default: return null;
        }
    }
}
