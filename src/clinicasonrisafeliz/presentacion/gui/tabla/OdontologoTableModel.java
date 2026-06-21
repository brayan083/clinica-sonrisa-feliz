package clinicasonrisafeliz.presentacion.gui.tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import clinicasonrisafeliz.modelo.Odontologo;

/**
 * Modelo de tabla para mostrar odontólogos en un {@link javax.swing.JTable}.
 * Mismo enfoque que {@link PacienteTableModel}: opera sobre objetos del dominio.
 */
public class OdontologoTableModel extends AbstractTableModel {

    private static final String[] COLUMNAS = {"ID", "Nombre", "Apellido", "Matrícula", "Email"};

    private List<Odontologo> odontologos = new ArrayList<>();

    public void setOdontologos(List<Odontologo> odontologos) {
        this.odontologos = (odontologos != null) ? odontologos : new ArrayList<>();
        fireTableDataChanged();
    }

    public Odontologo getOdontologoEn(int fila) {
        if (fila < 0 || fila >= odontologos.size()) return null;
        return odontologos.get(fila);
    }

    @Override
    public int getRowCount() { return odontologos.size(); }

    @Override
    public int getColumnCount() { return COLUMNAS.length; }

    @Override
    public String getColumnName(int columna) { return COLUMNAS[columna]; }

    @Override
    public boolean isCellEditable(int fila, int columna) { return false; }

    @Override
    public Object getValueAt(int fila, int columna) {
        Odontologo o = odontologos.get(fila);
        return switch (columna) {
            case 0 -> o.getId();
            case 1 -> o.getNombre();
            case 2 -> o.getApellido();
            case 3 -> o.getMatricula();
            case 4 -> o.getEmail();
            default -> "";
        };
    }
}
