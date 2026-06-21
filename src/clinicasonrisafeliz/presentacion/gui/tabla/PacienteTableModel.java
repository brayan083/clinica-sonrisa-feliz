package clinicasonrisafeliz.presentacion.gui.tabla;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import clinicasonrisafeliz.modelo.Paciente;

/**
 * Modelo de tabla para mostrar pacientes en un {@link javax.swing.JTable}.
 *
 * Extiende {@link AbstractTableModel} (en lugar de DefaultTableModel) para
 * trabajar directamente con objetos {@link Paciente} del dominio, sin tener
 * que volcar y reconstruir los datos celda por celda. Así la tabla y el
 * modelo de negocio quedan sincronizados de forma natural.
 */
public class PacienteTableModel extends AbstractTableModel {

    private static final String[] COLUMNAS = {"ID", "Nombre", "Apellido", "DNI", "Email", "Domicilio"};

    private List<Paciente> pacientes = new ArrayList<>();

    /** Reemplaza el contenido de la tabla y notifica a la vista para que se redibuje. */
    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = (pacientes != null) ? pacientes : new ArrayList<>();
        fireTableDataChanged();
    }

    /** Devuelve el paciente ubicado en la fila indicada (o null si está fuera de rango). */
    public Paciente getPacienteEn(int fila) {
        if (fila < 0 || fila >= pacientes.size()) return null;
        return pacientes.get(fila);
    }

    @Override
    public int getRowCount() { return pacientes.size(); }

    @Override
    public int getColumnCount() { return COLUMNAS.length; }

    @Override
    public String getColumnName(int columna) { return COLUMNAS[columna]; }

    @Override
    public boolean isCellEditable(int fila, int columna) { return false; }

    @Override
    public Object getValueAt(int fila, int columna) {
        Paciente p = pacientes.get(fila);
        return switch (columna) {
            case 0 -> p.getId();
            case 1 -> p.getNombre();
            case 2 -> p.getApellido();
            case 3 -> p.getDni();
            case 4 -> p.getEmail();
            case 5 -> (p.getDomicilio() != null) ? p.getDomicilio().toString() : "—";
            default -> "";
        };
    }
}
