package clinicasonrisafeliz;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import clinicasonrisafeliz.presentacion.gui.PantallaCarga;
import clinicasonrisafeliz.presentacion.gui.VentanaLogin;

/**
 * Punto de entrada de la aplicación con interfaz gráfica (Swing).
 *
 * Flujo de arranque:
 *   1. Muestra una pantalla de carga con barra de progreso.
 *   2. Construye el {@link ContextoAplicacion} en segundo plano mediante un
 *      {@link SwingWorker} (la lectura de los CSV no bloquea el hilo de eventos).
 *   3. Al terminar, cierra la pantalla de carga y abre la ventana de login.
 *
 * Para usar la versión por consola de entregas anteriores, ejecutar
 * {@link MainConsola} en su lugar.
 */
public class Main {

    public static void main(String[] args) {
        aplicarLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            PantallaCarga splash = new PantallaCarga();
            splash.setVisible(true);

            // SwingWorker: arma el contexto (lee los CSV) sin congelar la UI.
            new SwingWorker<ContextoAplicacion, Void>() {
                @Override
                protected ContextoAplicacion doInBackground() {
                    return new ContextoAplicacion();
                }

                @Override
                protected void done() {
                    splash.dispose();
                    try {
                        ContextoAplicacion contexto = get();
                        new VentanaLogin(contexto).setVisible(true);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                "No se pudo iniciar la aplicación: " + e.getMessage(),
                                "Error fatal", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                }
            }.execute();
        });
    }

    /** Aplica el Look & Feel del sistema operativo; si falla, usa el de Swing por defecto. */
    private static void aplicarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Look & Feel por defecto: no es crítico, seguimos sin interrumpir.
        }
    }
}
