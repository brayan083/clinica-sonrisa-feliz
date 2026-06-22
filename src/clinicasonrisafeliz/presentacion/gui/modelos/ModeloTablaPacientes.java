package clinicasonrisafeliz.presentacion.gui.modelos;

import clinicasonrisafeliz.modelo.Paciente;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaPacientes extends AbstractTableModel {

    private final String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Email"};
    private List<Paciente> pacientes;

    public ModeloTablaPacientes() {
        this.pacientes = new ArrayList<>();
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
        fireTableDataChanged();
    }

    public Paciente getPacienteAt(int rowIndex) {
        return pacientes.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return pacientes.size();
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
        Paciente p = pacientes.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getId();
            case 1: return p.getNombre();
            case 2: return p.getApellido();
            case 3: return p.getDni();
            case 4: return p.getEmail();
            default: return null;
        }
    }
}
