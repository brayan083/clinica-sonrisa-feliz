package clinicasonrisafeliz.presentacion.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 * Pantalla de carga (splash) que se muestra mientras un {@link javax.swing.SwingWorker}
 * inicializa la aplicación en segundo plano (lectura de archivos CSV).
 *
 * Incluye una barra de progreso indeterminada como indicador de carga, de modo
 * que la interfaz no parezca congelada durante la operación de disco.
 */
public class PantallaCarga extends JWindow {

    public PantallaCarga() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 120, 180), 2),
                BorderFactory.createEmptyBorder(24, 32, 24, 32)));

        JLabel titulo = new JLabel("Clínica Sonrisa Feliz", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));

        JLabel detalle = new JLabel("Cargando datos…", SwingConstants.CENTER);
        detalle.setFont(detalle.getFont().deriveFont(Font.PLAIN, 13f));

        JProgressBar barra = new JProgressBar();
        barra.setIndeterminate(true); // indicador de actividad mientras se carga

        panel.add(titulo,  BorderLayout.NORTH);
        panel.add(detalle, BorderLayout.CENTER);
        panel.add(barra,   BorderLayout.SOUTH);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
    }
}
