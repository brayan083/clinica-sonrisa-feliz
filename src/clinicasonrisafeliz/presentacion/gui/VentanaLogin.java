package clinicasonrisafeliz.presentacion.gui;

import clinicasonrisafeliz.controlador.ControladorRecepcionista;
import clinicasonrisafeliz.modelo.Recepcionista;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JDialog {

    private final ControladorRecepcionista controlador;
    private Recepcionista recepcionistaAutenticado;

    private JTextField txtLegajoLogin;
    private JTextField txtNombreReg, txtApellidoReg, txtEmailReg, txtLegajoReg;

    public VentanaLogin(JFrame parent, ControladorRecepcionista controlador) {
        super(parent, "Clínica Sonrisa Feliz - Acceso", true);
        this.controlador = controlador;
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel de Login
        JPanel panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelLogin.add(new JLabel("Legajo:"), gbc);
        txtLegajoLogin = new JTextField(15);
        gbc.gridx = 1;
        panelLogin.add(txtLegajoLogin, gbc);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(e -> login());
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panelLogin.add(btnLogin, gbc);

        // Panel de Registro
        JPanel panelReg = new JPanel(new GridBagLayout());
        panelReg.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 0; panelReg.add(new JLabel("Nombre:"), gbc);
        txtNombreReg = new JTextField(15); gbc.gridx = 1; panelReg.add(txtNombreReg, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelReg.add(new JLabel("Apellido:"), gbc);
        txtApellidoReg = new JTextField(15); gbc.gridx = 1; panelReg.add(txtApellidoReg, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelReg.add(new JLabel("Email:"), gbc);
        txtEmailReg = new JTextField(15); gbc.gridx = 1; panelReg.add(txtEmailReg, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelReg.add(new JLabel("Legajo:"), gbc);
        txtLegajoReg = new JTextField(15); gbc.gridx = 1; panelReg.add(txtLegajoReg, gbc);

        JButton btnReg = new JButton("Registrarse");
        btnReg.addActionListener(e -> registrar());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelReg.add(btnReg, gbc);

        tabbedPane.addTab("Iniciar Sesión", panelLogin);
        tabbedPane.addTab("Registrarse", panelReg);

        add(tabbedPane);
    }

    private void login() {
        String legajo = txtLegajoLogin.getText().trim();
        if (legajo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El legajo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Recepcionista r = controlador.buscarPorLegajo(legajo);
        if (r == null) {
            JOptionPane.showMessageDialog(this, "Legajo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            this.recepcionistaAutenticado = r;
            dispose();
        }
    }

    private void registrar() {
        String nombre = txtNombreReg.getText().trim();
        String apellido = txtApellidoReg.getText().trim();
        String email = txtEmailReg.getText().trim();
        String legajo = txtLegajoReg.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || legajo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            this.recepcionistaAutenticado = controlador.registrar(nombre, apellido, email, legajo);
            JOptionPane.showMessageDialog(this, "Registro exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Recepcionista getRecepcionistaAutenticado() {
        return recepcionistaAutenticado;
    }
}
