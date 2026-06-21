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
        CardLayout cardLayout = new CardLayout();
        JPanel panelContenedor = new JPanel(cardLayout);
        
        // ── PANEL DE LOGIN ──
        JPanel panelLoginWrapper = new JPanel(new BorderLayout());
        panelLoginWrapper.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel lblTituloLogin = new JLabel("Bienvenido a Sonrisa Feliz", SwingConstants.CENTER);
        lblTituloLogin.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTituloLogin.setForeground(new Color(53, 79, 82));
        lblTituloLogin.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelLoginWrapper.add(lblTituloLogin, BorderLayout.NORTH);

        JPanel panelLogin = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelLogin.add(new JLabel("Legajo:"), gbc);
        txtLegajoLogin = new JTextField(15);
        gbc.gridx = 1;
        panelLogin.add(txtLegajoLogin, gbc);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(82, 121, 111));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(e -> login());
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panelLogin.add(btnLogin, gbc);

        JButton btnIrARegistro = new JButton("¿No tienes cuenta? Regístrate");
        btnIrARegistro.setContentAreaFilled(false);
        btnIrARegistro.setBorderPainted(false);
        btnIrARegistro.setForeground(new Color(82, 121, 111));
        btnIrARegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrARegistro.addActionListener(e -> {
            cardLayout.show(panelContenedor, "REGISTRO");
            pack();
            setLocationRelativeTo(getParent());
        });
        gbc.gridy = 2; gbc.insets = new Insets(5, 10, 10, 10);
        panelLogin.add(btnIrARegistro, gbc);

        panelLoginWrapper.add(panelLogin, BorderLayout.CENTER);

        // ── PANEL DE REGISTRO ──
        JPanel panelRegWrapper = new JPanel(new BorderLayout());
        panelRegWrapper.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel lblTituloReg = new JLabel("Crear Nueva Cuenta", SwingConstants.CENTER);
        lblTituloReg.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTituloReg.setForeground(new Color(53, 79, 82));
        lblTituloReg.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelRegWrapper.add(lblTituloReg, BorderLayout.NORTH);

        JPanel panelReg = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; panelReg.add(new JLabel("Nombre:"), gbc);
        txtNombreReg = new JTextField(15); gbc.gridx = 1; panelReg.add(txtNombreReg, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelReg.add(new JLabel("Apellido:"), gbc);
        txtApellidoReg = new JTextField(15); gbc.gridx = 1; panelReg.add(txtApellidoReg, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelReg.add(new JLabel("Email:"), gbc);
        txtEmailReg = new JTextField(15); gbc.gridx = 1; panelReg.add(txtEmailReg, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelReg.add(new JLabel("Legajo:"), gbc);
        txtLegajoReg = new JTextField(15); gbc.gridx = 1; panelReg.add(txtLegajoReg, gbc);

        JButton btnReg = new JButton("Registrarse");
        btnReg.setBackground(new Color(82, 121, 111));
        btnReg.setForeground(Color.WHITE);
        btnReg.setFocusPainted(false);
        btnReg.addActionListener(e -> registrar());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panelReg.add(btnReg, gbc);

        JButton btnIrALogin = new JButton("¿Ya tienes cuenta? Inicia Sesión");
        btnIrALogin.setContentAreaFilled(false);
        btnIrALogin.setBorderPainted(false);
        btnIrALogin.setForeground(new Color(82, 121, 111));
        btnIrALogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIrALogin.addActionListener(e -> {
            cardLayout.show(panelContenedor, "LOGIN");
            pack();
            setLocationRelativeTo(getParent());
        });
        gbc.gridy = 5; gbc.insets = new Insets(5, 10, 10, 10);
        panelReg.add(btnIrALogin, gbc);

        panelRegWrapper.add(panelReg, BorderLayout.CENTER);

        panelContenedor.add(panelLoginWrapper, "LOGIN");
        panelContenedor.add(panelRegWrapper, "REGISTRO");

        add(panelContenedor);
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
