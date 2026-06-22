package clinicasonrisafeliz.presentacion.gui;

import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.presentacion.gui.modelos.ModeloTablaPacientes;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelPacientes extends JPanel {

    private final ControladorPaciente controlador;
    private final ModeloTablaPacientes modeloTabla;
    private JTable tabla;

    private JTextField txtId, txtNombre, txtApellido, txtDni, txtEmail;
    private JTextField txtCalle, txtNumero, txtLocalidad, txtProvincia;
    private JButton btnGuardar, btnEliminar, btnLimpiar;

    public PanelPacientes(ControladorPaciente controlador) {
        this.controlador = controlador;
        this.modeloTabla = new ModeloTablaPacientes();
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    cargarFormulario(modeloTabla.getPacienteAt(filaSeleccionada));
                }
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del Paciente", TitledBorder.LEFT, TitledBorder.TOP));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(); txtId.setEditable(false);
        txtNombre = new JTextField(15);
        txtApellido = new JTextField(15);
        txtDni = new JTextField(15);
        txtEmail = new JTextField(15);
        txtCalle = new JTextField(15);
        txtNumero = new JTextField(15);
        txtLocalidad = new JTextField(15);
        txtProvincia = new JTextField(15);

        int row = 0;
        agregarCampo(panelFormulario, gbc, "ID:", txtId, 0, row++);
        agregarCampo(panelFormulario, gbc, "Nombre:", txtNombre, 0, row);
        agregarCampo(panelFormulario, gbc, "Calle:", txtCalle, 2, row++);
        
        agregarCampo(panelFormulario, gbc, "Apellido:", txtApellido, 0, row);
        agregarCampo(panelFormulario, gbc, "Número:", txtNumero, 2, row++);

        agregarCampo(panelFormulario, gbc, "DNI:", txtDni, 0, row);
        agregarCampo(panelFormulario, gbc, "Localidad:", txtLocalidad, 2, row++);

        agregarCampo(panelFormulario, gbc, "Email:", txtEmail, 0, row);
        agregarCampo(panelFormulario, gbc, "Provincia:", txtProvincia, 2, row++);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnGuardar = new JButton("Guardar / Actualizar");
        btnGuardar.setBackground(new Color(82, 121, 111));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(217, 83, 79)); // Rojo
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);

        btnLimpiar = new JButton("Limpiar / Nuevo");
        btnLimpiar.setFocusPainted(false);

        btnGuardar.addActionListener(e -> guardarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnGuardar);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int col, int row) {
        gbc.gridx = col; gbc.gridy = row; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lbl, gbc);
        gbc.gridx = col + 1; gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void cargarDatos() {
        modeloTabla.setPacientes(controlador.listarTodos());
    }

    private void cargarFormulario(Paciente p) {
        txtId.setText(String.valueOf(p.getId()));
        txtNombre.setText(p.getNombre());
        txtApellido.setText(p.getApellido());
        txtDni.setText(p.getDni());
        txtEmail.setText(p.getEmail());
        if (p.getDomicilio() != null) {
            txtCalle.setText(p.getDomicilio().getCalle());
            txtNumero.setText(p.getDomicilio().getNumero());
            txtLocalidad.setText(p.getDomicilio().getLocalidad());
            txtProvincia.setText(p.getDomicilio().getProvincia());
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtEmail.setText("");
        txtCalle.setText("");
        txtNumero.setText("");
        txtLocalidad.setText("");
        txtProvincia.setText("");
        tabla.clearSelection();
        resetBorders();
    }

    private void resetBorders() {
        Color defaultColor = UIManager.getColor("TextField.borderColor");
        txtNombre.setBorder(BorderFactory.createLineBorder(defaultColor));
        txtApellido.setBorder(BorderFactory.createLineBorder(defaultColor));
        txtDni.setBorder(BorderFactory.createLineBorder(defaultColor));
        txtEmail.setBorder(BorderFactory.createLineBorder(defaultColor));
    }

    private void markInvalid(JTextField field) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    private void guardarPaciente() {
        resetBorders();
        String idStr = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDni.getText().trim();
        String email = txtEmail.getText().trim();
        
        boolean valid = true;
        if (nombre.isEmpty()) { markInvalid(txtNombre); valid = false; }
        if (apellido.isEmpty()) { markInvalid(txtApellido); valid = false; }
        if (dni.isEmpty()) { markInvalid(txtDni); valid = false; }
        if (email.isEmpty() || !email.contains("@")) { markInvalid(txtEmail); valid = false; }

        if (!valid) {
            JOptionPane.showMessageDialog(this, "Por favor corrija los campos marcados en rojo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (idStr.isEmpty()) {
                // Nuevo
                Domicilio dom = new Domicilio(txtCalle.getText().trim(), txtNumero.getText().trim(),
                        txtLocalidad.getText().trim(), txtProvincia.getText().trim());
                controlador.registrar(nombre, apellido, email, dni, dom);
                JOptionPane.showMessageDialog(this, "Paciente registrado con éxito.");
            } else {
                // Actualizar (solo nombre, apellido y email según controlador actual)
                controlador.actualizar(Long.parseLong(idStr), nombre, apellido, email);
                JOptionPane.showMessageDialog(this, "Paciente actualizado con éxito.");
            }
            limpiarFormulario();
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPaciente() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente de la tabla para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este paciente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controlador.eliminar(Long.parseLong(idStr));
                JOptionPane.showMessageDialog(this, "Paciente eliminado con éxito.");
                limpiarFormulario();
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
