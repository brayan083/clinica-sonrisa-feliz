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
        JPanel sidebar = new JPanel(new GridLayout(6, 1, 10, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setBackground(new Color(44, 62, 80));

        JLabel lblUser = new JLabel("<html><div style='text-align: center; color: white;'>Bienvenido/a<br><b>" 
                                    + operador.getNombreCompleto() + "</b></div></html>");
        lblUser.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(lblUser);

        JButton btnPacientes = crearBotonSidebar("Gestión Pacientes");
        JButton btnOdontologos = crearBotonSidebar("Gestión Odontólogos");
        JButton btnTurnos = crearBotonSidebar("Gestión Turnos");
        JButton btnBusquedas = crearBotonSidebar("Búsquedas");
        JButton btnSalir = crearBotonSidebar("Salir del Sistema");

        sidebar.add(btnPacientes);
        sidebar.add(btnOdontologos);
        sidebar.add(btnTurnos);
        sidebar.add(btnBusquedas);
        sidebar.add(btnSalir);

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
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 73, 94));
        btn.setFocusPainted(false);
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
