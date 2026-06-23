package clinicasonrisafeliz.presentacion.gui;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.controlador.ControladorRecepcionista;
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
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

public class PanelTurnos extends JPanel {

    private final ControladorTurno controladorTurno;
    private final ControladorPaciente controladorPaciente;
    private final ControladorOdontologo controladorOdontologo;
    private final ControladorRecepcionista controladorRecepcionista;
    private final Recepcionista operador;
    private final ModeloTablaTurnos modeloTabla;
    private JTable tabla;

    private JComboBox<Paciente> comboPacientes;
    private JComboBox<Odontologo> comboOdontologos;
    private JSpinner spinFecha;
    private JComboBox<String> comboHora;
    private JTextField txtId;
    private JButton btnReservar, btnModificar, btnConfirmar, btnCancelar, btnCompletar, btnLimpiar;

    public PanelTurnos(ControladorTurno ctrlTurno, ControladorPaciente ctrlPac, ControladorOdontologo ctrlOdon, ControladorRecepcionista ctrlRec, Recepcionista operador) {
        this.controladorTurno = ctrlTurno;
        this.controladorPaciente = ctrlPac;
        this.controladorOdontologo = ctrlOdon;
        this.controladorRecepcionista = ctrlRec;
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
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(15); txtId.setEditable(false);
        spinFecha = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinFecha, "yyyy-MM-dd");
        spinFecha.setEditor(dateEditor);
        
        comboHora = new JComboBox<>();
        for (int h = 8; h <= 18; h++) {
            String hs = String.format("%02d", h);
            comboHora.addItem(hs + ":00");
            if (h != 18) comboHora.addItem(hs + ":30");
        }
        
        comboPacientes = new JComboBox<>();
        comboPacientes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Paciente) {
                    Paciente p = (Paciente) value;
                    setText(p.getDni() + " - " + p.getApellido() + ", " + p.getNombre());
                }
                return this;
            }
        });

        comboOdontologos = new JComboBox<>();
        comboOdontologos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Odontologo) {
                    Odontologo o = (Odontologo) value;
                    setText(o.getMatricula() + " - " + o.getApellido() + ", " + o.getNombre());
                }
                return this;
            }
        });
        actualizarCombos();

        int row = 0;
        agregarCampo(panelFormulario, gbc, "ID Turno:", txtId, 0, row++);
        agregarCampo(panelFormulario, gbc, "Paciente:", comboPacientes, 0, row);
        agregarCampo(panelFormulario, gbc, "Fecha:", spinFecha, 2, row++);
        
        agregarCampo(panelFormulario, gbc, "Odontólogo:", comboOdontologos, 0, row);
        agregarCampo(panelFormulario, gbc, "Hora:", comboHora, 2, row++);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnLimpiar = new JButton("Limpiar / Nuevo");
        btnLimpiar.setFocusPainted(false);

        btnReservar = new JButton("Reservar");
        btnReservar.setBackground(new Color(82, 121, 111));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFocusPainted(false);

        btnModificar = new JButton("Modificar Fecha/Hora");
        btnModificar.setBackground(new Color(132, 169, 140)); // Verde claro
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFocusPainted(false);

        btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBackground(new Color(82, 121, 111));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);

        btnCancelar = new JButton("Cancelar Turno");
        btnCancelar.setBackground(new Color(217, 83, 79)); // Rojo
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        btnCompletar = new JButton("Completar Turno");
        btnCompletar.setBackground(new Color(92, 184, 92)); // Verde
        btnCompletar.setForeground(Color.WHITE);
        btnCompletar.setFocusPainted(false);

        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnReservar.addActionListener(e -> reservarTurno());
        btnModificar.addActionListener(e -> modificarTurno());
        btnConfirmar.addActionListener(e -> confirmarTurno());
        btnCancelar.addActionListener(e -> cancelarTurno());
        btnCompletar.addActionListener(e -> completarTurno());

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnCompletar);
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnReservar);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int col, int row) {
        gbc.gridx = col; gbc.gridy = row; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lbl, gbc);
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
        Date date = Date.from(t.getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant());
        spinFecha.setValue(date);
        comboHora.setSelectedItem(t.getHora().toString());
        comboPacientes.setSelectedItem(t.getPaciente());
        comboOdontologos.setSelectedItem(t.getOdontologo());
        resetBorders();
    }

    private void limpiarFormulario() {
        txtId.setText("");
        spinFecha.setValue(new Date());
        if (comboHora.getItemCount() > 0) comboHora.setSelectedIndex(0);
        tabla.clearSelection();
        resetBorders();
    }

    private void resetBorders() {
    }

    private void markInvalid(JComponent field) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    private void reservarTurno() {
        resetBorders();

        if (!controladorRecepcionista.hayRecepcionistas()) {
            JOptionPane.showMessageDialog(this, "No hay recepcionistas registrados. No se pueden reservar turnos sin un recepcionista.",
                "Operación no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Paciente p = (Paciente) comboPacientes.getSelectedItem();
        Odontologo o = (Odontologo) comboOdontologos.getSelectedItem();

        if (p == null || o == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar paciente y odontólogo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fecha = ((Date) spinFecha.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime hora = LocalTime.parse(comboHora.getSelectedItem().toString());

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
        LocalDate fecha = ((Date) spinFecha.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime hora = LocalTime.parse(comboHora.getSelectedItem().toString());

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

    private void completarTurno() {
        String idStr = txtId.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un turno.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Turno turno = controladorTurno.buscarPorId(Long.parseLong(idStr));
            if (!turno.getEstado().equals(EstadoTurno.CONFIRMADO)) {
                JOptionPane.showMessageDialog(this, "Solo se pueden completar turnos confirmados. Estado actual: " + turno.getEstado(),
                    "Operación inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Completar este turno?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controladorTurno.completar(Long.parseLong(idStr));
                JOptionPane.showMessageDialog(this, "Turno completado exitosamente.");
                limpiarFormulario();
                cargarDatosFondo();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
