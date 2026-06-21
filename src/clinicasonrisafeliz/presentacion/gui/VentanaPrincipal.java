package clinicasonrisafeliz.presentacion.gui;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.controlador.ControladorTurno;
import clinicasonrisafeliz.io.PersistenciaCSV;
import clinicasonrisafeliz.modelo.Recepcionista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {

    private final ControladorPaciente controladorPaciente;
    private final ControladorOdontologo controladorOdontologo;
    private final ControladorTurno controladorTurno;
    private final Recepcionista operador;

    private JPanel panelCentral;
    private CardLayout cardLayout;

    private PanelPacientes panelPacientes;
    private PanelOdontologos panelOdontologos;
    private PanelTurnos panelTurnos;
    private PanelBusquedas panelBusquedas;

    public VentanaPrincipal(ControladorPaciente cPac, ControladorOdontologo cOdon, ControladorTurno cTurno, Recepcionista operador) {
        super("Clínica Sonrisa Feliz - Sistema de Gestión");
        this.controladorPaciente = cPac;
        this.controladorOdontologo = cOdon;
        this.controladorTurno = cTurno;
        this.operador = operador;
        
        initComponents();
        configurarEventosCierre();
        
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Menú Lateral (Sidebar)
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        sidebar.setBackground(new Color(53, 79, 82)); // Charcoal

        JPanel panelBotonesArriba = new JPanel(new GridLayout(5, 1, 15, 15));
        panelBotonesArriba.setBackground(new Color(53, 79, 82));

        JLabel lblUser = new JLabel("<html><div style='text-align: center; color: white;'>Bienvenido/a<br><b>" 
                                    + operador.getNombreCompleto() + "</b></div></html>");
        lblUser.setHorizontalAlignment(SwingConstants.CENTER);
        panelBotonesArriba.add(lblUser);

        JButton btnPacientes = crearBotonSidebar("Gestión Pacientes");
        JButton btnOdontologos = crearBotonSidebar("Gestión Odontólogos");
        JButton btnTurnos = crearBotonSidebar("Gestión Turnos");
        JButton btnBusquedas = crearBotonSidebar("Búsquedas");

        panelBotonesArriba.add(btnPacientes);
        panelBotonesArriba.add(btnOdontologos);
        panelBotonesArriba.add(btnTurnos);
        panelBotonesArriba.add(btnBusquedas);

        sidebar.add(panelBotonesArriba, BorderLayout.NORTH);

        JButton btnSalir = crearBotonSidebar("Salir del Sistema");
        btnSalir.setBackground(new Color(169, 46, 34)); // Rojo oscuro para salir
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnSalir.setBackground(new Color(200, 60, 45)); // Hover rojo
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnSalir.setBackground(new Color(169, 46, 34));
            }
        });
        sidebar.add(btnSalir, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        // Panel Central con CardLayout
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        panelPacientes = new PanelPacientes(controladorPaciente);
        panelOdontologos = new PanelOdontologos(controladorOdontologo);
        panelTurnos = new PanelTurnos(controladorTurno, controladorPaciente, controladorOdontologo, operador);
        panelBusquedas = new PanelBusquedas(controladorTurno, controladorPaciente, controladorOdontologo);

        panelCentral.add(crearPanelBienvenida(), "BIENVENIDA");
        panelCentral.add(panelPacientes, "PACIENTES");
        panelCentral.add(panelOdontologos, "ODONTOLOGOS");
        panelCentral.add(panelTurnos, "TURNOS");
        panelCentral.add(panelBusquedas, "BUSQUEDAS");

        add(panelCentral, BorderLayout.CENTER);

        // Acciones de los botones
        btnPacientes.addActionListener(e -> cardLayout.show(panelCentral, "PACIENTES"));
        btnOdontologos.addActionListener(e -> cardLayout.show(panelCentral, "ODONTOLOGOS"));
        btnTurnos.addActionListener(e -> {
            panelTurnos.actualizarCombos();
            cardLayout.show(panelCentral, "TURNOS");
        });
        btnBusquedas.addActionListener(e -> cardLayout.show(panelCentral, "BUSQUEDAS"));
        btnSalir.addActionListener(e -> cerrarAplicacion());
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Seleccione una opción del menú lateral para comenzar.", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBotonSidebar(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Roboto", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(82, 121, 111)); // Sage Green
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(132, 169, 140)); // Light Sage Hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(82, 121, 111)); // Back to Sage Green
            }
        });

        return btn;
    }

    private void configurarEventosCierre() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });
    }

    private void cerrarAplicacion() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir?\nLos datos se guardarán automáticamente.", "Salir", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
