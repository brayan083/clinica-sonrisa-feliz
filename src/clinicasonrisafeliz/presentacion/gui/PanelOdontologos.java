package clinicasonrisafeliz.presentacion.gui;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.presentacion.gui.modelos.ModeloTablaOdontologos;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelOdontologos extends JPanel {

    private final ControladorOdontologo controlador;
    private final ModeloTablaOdontologos modeloTabla;
    private JTable tabla;

    private JTextField txtId, txtNombre, txtApellido, txtMatricula, txtEmail;
    private JButton btnGuardar, btnEliminar, btnLimpiar;

    public PanelOdontologos(ControladorOdontologo controlador) {
        this.controlador = controlador;
        this.modeloTabla = new ModeloTablaOdontologos();
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
                    cargarFormulario(modeloTabla.getOdontologoAt(filaSeleccionada));
                }
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos del Odontólogo", TitledBorder.LEFT, TitledBorder.TOP));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(); txtId.setEditable(false);
        txtNombre = new JTextField(15);
        txtApellido = new JTextField(15);
        txtMatricula = new JTextField(15);
        txtEmail = new JTextField(15);

        int row = 0;
        agregarCampo(panelFormulario, gbc, "ID:", txtId, 0, row++);
        agregarCampo(panelFormulario, gbc, "Nombre:", txtNombre, 0, row);
        agregarCampo(panelFormulario, gbc, "Apellido:", txtApellido, 2, row++);
        
        agregarCampo(panelFormulario, gbc, "Matrícula:", txtMatricula, 0, row);
        agregarCampo(panelFormulario, gbc, "Email:", txtEmail, 2, row++);

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

        btnGuardar.addActionListener(e -> guardarOdontologo());
        btnEliminar.addActionListener(e -> eliminarOdontologo());
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
        modeloTabla.setOdontologos(controlador.listarTodos());
    }

    private void cargarFormulario(Odontologo o) {
        txtId.setText(String.valueOf(o.getId()));
        txtNombre.setText(o.getNombre());
        txtApellido.setText(o.getApellido());
        txtMatricula.setText(o.getMatricula());
        txtMatricula.setEditable(false);
        txtEmail.setText(o.getEmail());
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtMatricula.setText("");
        txtMatricula.setEditable(true);
        txtEmail.setText("");
        tabla.clearSelection();
        resetBorders();
    }

    private void resetBorders() {
        Color defaultColor = UIManager.getColor("TextField.borderColor");
        txtNombre.setBorder(BorderFactory.createLineBorder(defaultColor));
        txtApellido.setBorder(BorderFactory.createLineBorder(defaultColor));
        txtMatricula.setBorder(BorderFactory.createLineBorder(defaultColor));
        txtEmail.setBorder(BorderFactory.createLineBorder(defaultColor));
    }

    private void markInvalid(JTextField field) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    private void guardarOdontologo() {
        resetBorders();
        String idStr = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String matricula = txtMatricula.getText().trim();
        String email = txtEmail.getText().trim();
        
        boolean valid = true;
        if (nombre.isEmpty()) { markInvalid(txtNombre); valid = false; }
        if (apellido.isEmpty()) { markInvalid(txtApellido); valid = false; }
        if (matricula.isEmpty()) { markInvalid(txtMatricula); valid = false; }
        if (email.isEmpty() || !email.contains("@")) { markInvalid(txtEmail); valid = false; }

        if (!valid) {
            JOptionPane.showMessageDialog(this, "Por favor corrija los campos marcados en rojo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (idStr.isEmpty()) {
                // Nuevo
                controlador.registrar(nombre, apellido, email, matricula);
                JOptionPane.showMessageDialog(this, "Odontólogo registrado con éxito.");
            } else {
                // Actualizar
                controlador.actualizar(Long.parseLong(idStr), nombre, apellido, email);
                JOptionPane.showMessageDialog(this, "Odontólogo actualizado con éxito.");
            }
            limpiarFormulario();
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarOdontologo() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un odontólogo de la tabla para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este odontólogo?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controlador.eliminar(Long.parseLong(idStr));
                JOptionPane.showMessageDialog(this, "Odontólogo eliminado con éxito.");
                limpiarFormulario();
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
