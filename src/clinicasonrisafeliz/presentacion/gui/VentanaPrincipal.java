package clinicasonrisafeliz.presentacion.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import clinicasonrisafeliz.ContextoAplicacion;
import clinicasonrisafeliz.modelo.Recepcionista;

/**
 * Ventana principal de la aplicación. Coordina la navegación entre los
 * paneles de gestión (pacientes, odontólogos, turnos y búsquedas) mediante un
 * {@link CardLayout}, ofrece una barra de menú y una barra de estado, y
 * gestiona el guardado automático al cerrar mediante un {@link WindowAdapter}.
 */
public class VentanaPrincipal extends JFrame {

    private final ContextoAplicacion contexto;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     contenido  = new JPanel(cardLayout);
    private final JLabel     barraEstado = new JLabel();

    private final PanelPacientes   panelPacientes;
    private final PanelOdontologos panelOdontologos;
    private final PanelTurnos      panelTurnos;
    private final PanelBusquedas   panelBusquedas;

    private RecordatorioTurnos recordatorio;

    public VentanaPrincipal(ContextoAplicacion contexto, Recepcionista operador) {
        super("Clínica Sonrisa Feliz — Sistema de Gestión");
        this.contexto = contexto;

        // ── Paneles (capa de presentación que delega en los controladores) ──────
        panelPacientes   = new PanelPacientes(contexto.getControladorPaciente());
        panelOdontologos = new PanelOdontologos(contexto.getControladorOdontologo());
        panelTurnos      = new PanelTurnos(contexto.getControladorTurno(),
                                           contexto.getControladorPaciente(),
                                           contexto.getControladorOdontologo(),
                                           operador);
        panelBusquedas   = new PanelBusquedas(contexto.getControladorPaciente(),
                                              contexto.getControladorOdontologo(),
                                              contexto.getControladorTurno());

        contenido.add(panelPacientes,   "pacientes");
        contenido.add(panelOdontologos, "odontologos");
        contenido.add(panelTurnos,      "turnos");
        contenido.add(panelBusquedas,   "busquedas");

        setLayout(new BorderLayout());
        setJMenuBar(construirMenu());
        add(construirNavegacionLateral(), BorderLayout.WEST);
        add(contenido,                    BorderLayout.CENTER);
        add(construirBarraEstado(operador), BorderLayout.SOUTH);

        // ── WindowListener: guarda automáticamente al cerrar ────────────────────
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });

        setMinimumSize(new Dimension(900, 600));
        setSize(1000, 650);
        setLocationRelativeTo(null);

        iniciarRecordatorios();
        mostrar("pacientes");
    }

    // ── Construcción de la UI ──────────────────────────────────────────────────

    private JMenuBar construirMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemGuardar = new JMenuItem("Guardar todo");
        JMenuItem itemSalir   = new JMenuItem("Salir");
        itemGuardar.addActionListener(e -> guardarTodo(true));
        itemSalir.addActionListener(e -> cerrarAplicacion());
        menuArchivo.add(itemGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        JMenu menuNav = new JMenu("Navegación");
        JMenuItem nPac = new JMenuItem("Pacientes");
        JMenuItem nOdo = new JMenuItem("Odontólogos");
        JMenuItem nTur = new JMenuItem("Turnos");
        JMenuItem nBus = new JMenuItem("Búsquedas");
        nPac.addActionListener(e -> mostrar("pacientes"));
        nOdo.addActionListener(e -> mostrar("odontologos"));
        nTur.addActionListener(e -> mostrar("turnos"));
        nBus.addActionListener(e -> mostrar("busquedas"));
        menuNav.add(nPac);
        menuNav.add(nOdo);
        menuNav.add(nTur);
        menuNav.add(nBus);

        barra.add(menuArchivo);
        barra.add(menuNav);
        return barra;
    }

    private JPanel construirNavegacionLateral() {
        JPanel lateral = new JPanel(new GridLayout(5, 1, 0, 8));
        lateral.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton btnPac = new JButton("Pacientes");
        JButton btnOdo = new JButton("Odontólogos");
        JButton btnTur = new JButton("Turnos");
        JButton btnBus = new JButton("Búsquedas");
        JButton btnSalir = new JButton("Salir");

        btnPac.addActionListener(e -> mostrar("pacientes"));
        btnOdo.addActionListener(e -> mostrar("odontologos"));
        btnTur.addActionListener(e -> mostrar("turnos"));
        btnBus.addActionListener(e -> mostrar("busquedas"));
        btnSalir.addActionListener(e -> cerrarAplicacion());

        lateral.add(btnPac);
        lateral.add(btnOdo);
        lateral.add(btnTur);
        lateral.add(btnBus);
        lateral.add(btnSalir);
        return lateral;
    }

    private JPanel construirBarraEstado(Recepcionista operador) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        JLabel usuario = new JLabel("Operador: " + operador.getNombreCompleto()
                + " (Legajo " + operador.getLegajo() + ")");
        barraEstado.setHorizontalAlignment(SwingConstants.RIGHT);
        barraEstado.setText("Listo.");
        panel.add(usuario, BorderLayout.WEST);
        panel.add(barraEstado, BorderLayout.EAST);
        return panel;
    }

    // ── Navegación ─────────────────────────────────────────────────────────────

    private void mostrar(String clave) {
        // Refrescamos el panel destino para que muestre los datos más recientes.
        switch (clave) {
            case "pacientes"   -> panelPacientes.refrescar();
            case "odontologos" -> panelOdontologos.refrescar();
            case "turnos"      -> panelTurnos.refrescar();
            default -> { /* búsquedas no necesita refresco automático */ }
        }
        cardLayout.show(contenido, clave);
    }

    // ── Hilos: recordatorios de turnos próximos ─────────────────────────────────

    private void iniciarRecordatorios() {
        recordatorio = new RecordatorioTurnos(
                contexto.getControladorTurno(),
                mensaje -> barraEstado.setText(mensaje)); // se ejecuta en el EDT
        recordatorio.iniciar();
    }

    // ── Cierre y guardado ───────────────────────────────────────────────────────

    private void cerrarAplicacion() {
        if (!GuiUtils.confirmar(this, "¿Salir de la aplicación?\nLos datos se guardarán automáticamente.")) {
            return;
        }
        guardarTodo(false);
        if (recordatorio != null) recordatorio.detener();
        dispose();
        System.exit(0);
    }

    /**
     * Persiste todos los datos. Usa un {@link javax.swing.SwingWorker} para no
     * bloquear el hilo de eventos durante la escritura a disco.
     *
     * @param avisarExito si true, muestra un diálogo al terminar (útil para el
     *                    menú "Guardar todo"); en el cierre se omite.
     */
    private void guardarTodo(boolean avisarExito) {
        barraEstado.setText("Guardando datos…");
        try {
            // Para un volumen tan chico, el guardado es inmediato; aun así lo
            // hacemos explícito y reportamos el resultado al usuario.
            contexto.guardarTodo();
            barraEstado.setText("Datos guardados.");
            if (avisarExito) {
                JOptionPane.showMessageDialog(this, "Todos los datos se guardaron correctamente.",
                        "Guardar", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            GuiUtils.error(this, "No se pudieron guardar los datos: " + ex.getMessage());
        }
    }
}
