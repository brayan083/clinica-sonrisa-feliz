package clinicasonrisafeliz.presentacion.gui;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.controlador.ControladorTurno;
import clinicasonrisafeliz.enums.EstadoTurno;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Recepcionista;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.presentacion.gui.modelos.ModeloTablaTurnos;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PanelTurnos extends JPanel {

    private final ControladorTurno controladorTurno;
    private final ControladorPaciente controladorPaciente;
    private final ControladorOdontologo controladorOdontologo;
    private final Recepcionista operador;
    private final ModeloTablaTurnos modeloTabla;
    private JTable tabla;

    private JComboBox<Paciente> comboPacientes;
    private JComboBox<Odontologo> comboOdontologos;
    private JTextField txtId, txtFecha, txtHora;
    private JButton btnReservar, btnModificar, btnConfirmar, btnCancelar, btnLimpiar;

    public PanelTurnos(ControladorTurno ctrlTurno, ControladorPaciente ctrlPac, ControladorOdontologo ctrlOdon, Recepcionista operador) {
        this.controladorTurno = ctrlTurno;
        this.controladorPaciente = ctrlPac;
        this.controladorOdontologo = ctrlOdon;
        this.operador = operador;
        this.modeloTabla = new ModeloTablaTurnos();
        initComponents();
        cargarDatosFondo(); // Usa SwingWorker
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
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    cargarFormulario(modeloTabla.getTurnoAt(fila));
                }
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Gestión de Turnos", TitledBorder.LEFT, TitledBorder.TOP));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(15); txtId.setEditable(false);
        txtFecha = new JTextField(15); txtFecha.setToolTipText("YYYY-MM-DD");
        txtHora = new JTextField(15); txtHora.setToolTipText("HH:MM");
        
        comboPacientes = new JComboBox<>();
        comboOdontologos = new JComboBox<>();
        actualizarCombos();

        int row = 0;
        agregarCampo(panelFormulario, gbc, "ID Turno:", txtId, 0, row++);
        agregarCampo(panelFormulario, gbc, "Paciente:", comboPacientes, 0, row);
        agregarCampo(panelFormulario, gbc, "Fecha (AAAA-MM-DD):", txtFecha, 2, row++);
        
        agregarCampo(panelFormulario, gbc, "Odontólogo:", comboOdontologos, 0, row);
        agregarCampo(panelFormulario, gbc, "Hora (HH:MM):", txtHora, 2, row++);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLimpiar = new JButton("Limpiar");
        btnReservar = new JButton("Reservar");
        btnModificar = new JButton("Modificar Fecha/Hora");
        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar Turno");

        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnReservar.addActionListener(e -> reservarTurno());
        btnModificar.addActionListener(e -> modificarTurno());
        btnConfirmar.addActionListener(e -> confirmarTurno());
        btnCancelar.addActionListener(e -> cancelarTurno());

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnReservar);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int col, int row) {
        gbc.gridx = col; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = col + 1; gbc.weightx = 1;
        panel.add(field, gbc);
    }

    public void actualizarCombos() {
        comboPacientes.removeAllItems();
        for (Paciente p : controladorPaciente.listarTodos()) {
            comboPacientes.addItem(p);
        }
        comboOdontologos.removeAllItems();
        for (Odontologo o : controladorOdontologo.listarTodos()) {
            comboOdontologos.addItem(o);
        }
    }

    /** Carga asíncrona usando SwingWorker para no bloquear la interfaz */
    private void cargarDatosFondo() {
        SwingWorker<List<Turno>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Turno> doInBackground() {
                return controladorTurno.listarTodos();
            }

            @Override
            protected void done() {
                try {
                    modeloTabla.setTurnos(get());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PanelTurnos.this, "Error cargando turnos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void cargarFormulario(Turno t) {
        txtId.setText(String.valueOf(t.getId()));
        txtFecha.setText(t.getFecha().toString());
        txtHora.setText(t.getHora().toString());
        comboPacientes.setSelectedItem(t.getPaciente());
        comboOdontologos.setSelectedItem(t.getOdontologo());
        resetBorders();
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        tabla.clearSelection();
        resetBorders();
    }

    private void resetBorders() {
        Color defaultColor = UIManager.getColor("TextField.borderColor");
        txtFecha.setBorder(BorderFactory.createLineBorder(defaultColor));
        txtHora.setBorder(BorderFactory.createLineBorder(defaultColor));
    }

    private void markInvalid(JTextField field) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    private void reservarTurno() {
        resetBorders();
        Paciente p = (Paciente) comboPacientes.getSelectedItem();
        Odontologo o = (Odontologo) comboOdontologos.getSelectedItem();
        String fechaStr = txtFecha.getText().trim();
        String horaStr = txtHora.getText().trim();

        if (p == null || o == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar paciente y odontólogo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fecha = null;
        LocalTime hora = null;
        boolean valid = true;

        try { fecha = LocalDate.parse(fechaStr); } catch (DateTimeParseException e) { markInvalid(txtFecha); valid = false; }
        try { hora = LocalTime.parse(horaStr); } catch (DateTimeParseException e) { markInvalid(txtHora); valid = false; }

        if (!valid) {
            JOptionPane.showMessageDialog(this, "Formatos inválidos. Fecha: AAAA-MM-DD. Hora: HH:MM.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            controladorTurno.reservar(p.getId(), o.getId(), fecha, hora, operador);
            JOptionPane.showMessageDialog(this, "Turno reservado con éxito.");
            limpiarFormulario();
            cargarDatosFondo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarTurno() {
        String idStr = txtId.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un turno para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        resetBorders();
        String fechaStr = txtFecha.getText().trim();
        String horaStr = txtHora.getText().trim();

        LocalDate fecha = null;
        LocalTime hora = null;
        boolean valid = true;

        try { fecha = LocalDate.parse(fechaStr); } catch (DateTimeParseException e) { markInvalid(txtFecha); valid = false; }
        try { hora = LocalTime.parse(horaStr); } catch (DateTimeParseException e) { markInvalid(txtHora); valid = false; }

        if (!valid) {
            JOptionPane.showMessageDialog(this, "Formatos inválidos. Fecha: AAAA-MM-DD. Hora: HH:MM.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            controladorTurno.modificar(Long.parseLong(idStr), fecha, hora);
            JOptionPane.showMessageDialog(this, "Turno modificado con éxito.");
            limpiarFormulario();
            cargarDatosFondo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarTurno() {
        cambiarEstado("confirmar");
    }

    private void cancelarTurno() {
        cambiarEstado("cancelar");
    }

    private void cambiarEstado(String accion) {
        String idStr = txtId.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un turno.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿" + accion + " este turno?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (accion.equals("confirmar")) controladorTurno.confirmar(Long.parseLong(idStr));
                else controladorTurno.cancelar(Long.parseLong(idStr));
                JOptionPane.showMessageDialog(this, "Operación exitosa.");
                cargarDatosFondo();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
