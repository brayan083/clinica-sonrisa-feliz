package clinicasonrisafeliz.presentacion.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import clinicasonrisafeliz.ContextoAplicacion;
import clinicasonrisafeliz.controlador.ControladorRecepcionista;
import clinicasonrisafeliz.modelo.Recepcionista;

/**
 * Ventana de inicio de sesión. Permite a una recepcionista ingresar por su
 * número de legajo o registrarse. Tras autenticarse correctamente, abre la
 * {@link VentanaPrincipal} y se cierra a sí misma.
 */
public class VentanaLogin extends JFrame {

    private final ContextoAplicacion       contexto;
    private final ControladorRecepcionista controlador;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     tarjetas   = new JPanel(cardLayout);

    // Campos de login
    private final JTextField txtLegajoLogin = new JTextField(15);

    // Campos de registro
    private final JTextField txtNombre   = new JTextField(15);
    private final JTextField txtApellido = new JTextField(15);
    private final JTextField txtEmail    = new JTextField(15);
    private final JTextField txtLegajoReg = new JTextField(15);

    public VentanaLogin(ContextoAplicacion contexto) {
        super("Clínica Sonrisa Feliz — Acceso");
        this.contexto    = contexto;
        this.controlador = contexto.getControladorRecepcionista();

        setLayout(new BorderLayout());
        add(construirEncabezado(), BorderLayout.NORTH);

        tarjetas.add(construirPanelLogin(),    "login");
        tarjetas.add(construirPanelRegistro(), "registro");
        add(tarjetas, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(460, 360));
        pack();
        setLocationRelativeTo(null);
    }

    private JLabel construirEncabezado() {
        JLabel titulo = new JLabel("Clínica Sonrisa Feliz", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        return titulo;
    }

    private JPanel construirPanelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Iniciar sesión"));
        GridBagConstraints g = baseConstraints();

        g.gridy = 0;
        g.gridx = 0; panel.add(new JLabel("Legajo:"), g);
        g.gridx = 1; panel.add(txtLegajoLogin, g);

        JButton btnIngresar = new JButton("Ingresar");
        JButton btnIrRegistro = new JButton("Registrarse");
        btnIngresar.addActionListener(e -> login());
        btnIrRegistro.addActionListener(e -> cardLayout.show(tarjetas, "registro"));

        g.gridy = 1; g.gridx = 0; panel.add(btnIngresar, g);
        g.gridx = 1; panel.add(btnIrRegistro, g);

        return panel;
    }

    private JPanel construirPanelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registrar nueva recepcionista"));
        GridBagConstraints g = baseConstraints();

        g.gridy = 0; g.gridx = 0; panel.add(new JLabel("Nombre:"),   g); g.gridx = 1; panel.add(txtNombre,   g);
        g.gridy = 1; g.gridx = 0; panel.add(new JLabel("Apellido:"), g); g.gridx = 1; panel.add(txtApellido, g);
        g.gridy = 2; g.gridx = 0; panel.add(new JLabel("Email:"),    g); g.gridx = 1; panel.add(txtEmail,    g);
        g.gridy = 3; g.gridx = 0; panel.add(new JLabel("Legajo:"),   g); g.gridx = 1; panel.add(txtLegajoReg, g);

        JButton btnRegistrar = new JButton("Registrar e ingresar");
        JButton btnVolver    = new JButton("Volver");
        btnRegistrar.addActionListener(e -> registrar());
        btnVolver.addActionListener(e -> cardLayout.show(tarjetas, "login"));

        g.gridy = 4; g.gridx = 0; panel.add(btnRegistrar, g);
        g.gridx = 1; panel.add(btnVolver, g);

        return panel;
    }

    private GridBagConstraints baseConstraints() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;
        return g;
    }

    // ── Acciones ────────────────────────────────────────────────────────────────

    private void login() {
        if (!GuiUtils.validarNoVacio(txtLegajoLogin)) {
            GuiUtils.error(this, "Ingresá tu número de legajo.");
            return;
        }
        try {
            Recepcionista r = controlador.buscarPorLegajo(txtLegajoLogin.getText().trim());
            if (r == null) {
                GuiUtils.error(this, "Legajo no encontrado. Verificá el dato o registrate.");
                return;
            }
            abrirVentanaPrincipal(r);
        } catch (Exception ex) {
            GuiUtils.error(this, ex.getMessage());
        }
    }

    private void registrar() {
        boolean ok = true;
        ok &= GuiUtils.validarNoVacio(txtNombre);
        ok &= GuiUtils.validarNoVacio(txtApellido);
        ok &= GuiUtils.validarEmail(txtEmail);
        ok &= GuiUtils.validarNoVacio(txtLegajoReg);
        if (!ok) {
            GuiUtils.error(this, "Revisá los campos marcados en rojo.");
            return;
        }
        try {
            Recepcionista r = controlador.registrar(
                    txtNombre.getText().trim(), txtApellido.getText().trim(),
                    txtEmail.getText().trim(), txtLegajoReg.getText().trim());
            GuiUtils.info(this, "Registro exitoso. ¡Bienvenido/a, " + r.getNombreCompleto() + "!");
            abrirVentanaPrincipal(r);
        } catch (Exception ex) {
            GuiUtils.error(this, ex.getMessage());
        }
    }

    private void abrirVentanaPrincipal(Recepcionista operador) {
        VentanaPrincipal principal = new VentanaPrincipal(contexto, operador);
        principal.setVisible(true);
        dispose(); // cerramos la ventana de login
    }
}
