package clinicasonrisafeliz.presentacion.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import clinicasonrisafeliz.enums.EstadoTurno;

/**
 * Renderer que pinta la columna "Estado" de la tabla de turnos con un color
 * según su valor, para que el usuario distinga de un vistazo en qué situación
 * está cada turno.
 */
public class EstadoTurnoRenderer extends DefaultTableCellRenderer {

    private static final Color PENDIENTE  = new Color(190, 120, 0);   // ámbar
    private static final Color CONFIRMADO = new Color(0, 130, 60);    // verde
    private static final Color CANCELADO  = new Color(190, 40, 40);   // rojo
    private static final Color COMPLETADO = new Color(90, 90, 90);    // gris

    @Override
    public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                                                   boolean foco, int fila, int columna) {
        Component c = super.getTableCellRendererComponent(tabla, valor, seleccionada, foco, fila, columna);
        c.setFont(c.getFont().deriveFont(Font.BOLD));

        // Cuando la fila está seleccionada respetamos el color de selección del sistema.
        if (seleccionada) {
            c.setForeground(tabla.getSelectionForeground());
            return c;
        }
        if (valor instanceof EstadoTurno estado) {
            c.setForeground(switch (estado) {
                case PENDIENTE  -> PENDIENTE;
                case CONFIRMADO -> CONFIRMADO;
                case CANCELADO  -> CANCELADO;
                case COMPLETADO -> COMPLETADO;
            });
        } else {
            c.setForeground(tabla.getForeground());
        }
        return c;
    }
}
