package clinicasonrisafeliz.presentacion.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.controlador.ControladorTurno;
import clinicasonrisafeliz.enums.EstadoTurno;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Recepcionista;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.presentacion.gui.tabla.TurnoTableModel;

/**
 * Panel de gestión de turnos.
 *
 * Distingue claramente dos tareas que antes se mezclaban:
 *   - <b>Reservar</b> un turno nuevo (formulario superior con sus propios campos).
 *   - <b>Operar</b> sobre un turno existente: confirmar, completar, cancelar o
 *     modificar su fecha/hora. La modificación se hace en un diálogo aparte
 *     (botón o doble clic en la fila), por lo que ya no comparte campos con la reserva.
 *
 * Los botones de acción se habilitan según el estado del turno seleccionado,
 * respetando las transiciones válidas del ciclo de vida ({@link EstadoTurno}).
 */
public class PanelTurnos extends JPanel {

    private final ControladorTurno      controladorTurno;
    private final ControladorPaciente   controladorPaciente;
    private final ControladorOdontologo controladorOdontologo;
    private final Recepcionista         operador;

    private final TurnoTableModel modelo = new TurnoTableModel();
    private final JTable          tabla  = new JTable(modelo);

    private final JComboBox<ItemCombo<Paciente>>   comboPacientes   = new JComboBox<>();
    private final JComboBox<ItemCombo<Odontologo>> comboOdontologos = new JComboBox<>();
    private final JSpinner spFechaReserva = GuiUtils.crearSpinnerFecha();
    private final JSpinner spHoraReserva  = GuiUtils.crearSpinnerHora();

    // Botones de acción (se guardan como campos para habilitarlos/deshabilitarlos)
    private final JButton btnConfirmar = new JButton("Confirmar");
    private final JButton btnCompletar = new JButton("Completar");
    private final JButton btnCancelar  = new JButton("Cancelar");
    private final JButton btnModificar = new JButton("Modificar fecha/hora");

    public PanelTurnos(ControladorTurno controladorTurno,
                       ControladorPaciente controladorPaciente,
                       ControladorOdontologo controladorOdontologo,
                       Recepcionista operador) {
        this.controladorTurno      = controladorTurno;
        this.controladorPaciente   = controladorPaciente;
        this.controladorOdontologo = controladorOdontologo;
        this.operador              = operador;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirFormularioReserva(), BorderLayout.NORTH);
        add(construirTabla(),             BorderLayout.CENTER);
        add(construirAcciones(),          BorderLayout.SOUTH);

        // Valores iniciales del formulario de reserva: hoy a las 09:00.
        GuiUtils.setHoraSpinner(spHoraReserva, LocalTime.of(9, 0));

        refrescar();
    }

    // ── Construcción de la UI ──────────────────────────────────────────────────

    private JPanel construirFormularioReserva() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Reservar nuevo turno"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;

        g.gridy = 0;
        g.gridx = 0; form.add(new JLabel("Paciente:"),   g);
        g.gridx = 1; form.add(comboPacientes,   g);
        g.gridx = 2; form.add(new JLabel("Odontólogo:"), g);
        g.gridx = 3; form.add(comboOdontologos, g);

        g.gridy = 1;
        g.gridx = 0; form.add(new JLabel("Fecha:"), g);
        g.gridx = 1; form.add(spFechaReserva, g);
        g.gridx = 2; form.add(new JLabel("Hora:"),  g);
        g.gridx = 3; form.add(spHoraReserva,  g);

        JButton btnReservar = new JButton("Reservar turno");
        btnReservar.addActionListener(e -> reservar());
        g.gridy = 2; g.gridx = 0; g.gridwidth = 4;
        form.add(btnReservar, g);

        return form;
    }

    private JScrollPane construirTabla() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(24);

        // Estado coloreado según su valor (columna 5 del TurnoTableModel).
        tabla.getColumnModel().getColumn(5).setCellRenderer(new EstadoTurnoRenderer());

        // Anchos sugeridos para que las columnas no queden apretadas.
        int[] anchos = {40, 90, 60, 160, 160, 110, 130};
        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        // Habilita/deshabilita los botones según el turno seleccionado.
        tabla.getSelectionModel().addListSelectionListener(e -> actualizarBotones());

        // MouseListener: doble clic en una fila abre el diálogo de modificación.
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && btnModificar.isEnabled()) {
                    modificar();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Turnos registrados — doble clic en una fila para modificar fecha/hora"));
        return scroll;
    }

    private JPanel construirAcciones() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 8, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Acciones sobre el turno seleccionado"));

        JButton btnRefrescar = new JButton("Refrescar");

        btnConfirmar.addActionListener(e -> cambiarEstado("confirmar"));
        btnCompletar.addActionListener(e -> cambiarEstado("completar"));
        btnCancelar.addActionListener(e -> cambiarEstado("cancelar"));
        btnModificar.addActionListener(e -> modificar());
        btnRefrescar.addActionListener(e -> { tabla.clearSelection(); refrescar(); });

        panel.add(btnConfirmar);
        panel.add(btnCompletar);
        panel.add(btnCancelar);
        panel.add(btnModificar);
        panel.add(btnRefrescar);
        return panel;
    }

    // ── Acciones ────────────────────────────────────────────────────────────────

    /** Recarga los combos (paciente/odontólogo) y la tabla de turnos. */
    public void refrescar() {
        recargarCombos();
        modelo.setTurnos(controladorTurno.listarTodos());
        actualizarBotones();
    }

    private void recargarCombos() {
        comboPacientes.removeAllItems();
        for (Paciente p : controladorPaciente.listarTodos()) {
            comboPacientes.addItem(new ItemCombo<>(
                    p.getNombreCompleto() + " (DNI " + p.getDni() + ")", p));
        }
        comboOdontologos.removeAllItems();
        for (Odontologo o : controladorOdontologo.listarTodos()) {
            comboOdontologos.addItem(new ItemCombo<>(
                    o.getNombreCompleto() + " (" + o.getMatricula() + ")", o));
        }
    }

    private void reservar() {
        ItemCombo<Paciente>   itemP = comboPacientes.getItemAt(comboPacientes.getSelectedIndex());
        ItemCombo<Odontologo> itemO = comboOdontologos.getItemAt(comboOdontologos.getSelectedIndex());
        if (itemP == null || itemO == null) {
            GuiUtils.advertencia(this, "Debe haber al menos un paciente y un odontólogo registrados.");
            return;
        }
        LocalDate fecha = GuiUtils.fechaDeSpinner(spFechaReserva);
        LocalTime hora  = GuiUtils.horaDeSpinner(spHoraReserva);
        try {
            Turno t = controladorTurno.reservar(
                    itemP.getValor().getId(), itemO.getValor().getId(), fecha, hora, operador);
            GuiUtils.info(this, "Turno reservado correctamente (ID " + t.getId() + ").");
            refrescar();
        } catch (Exception ex) {
            GuiUtils.error(this, ex.getMessage());
        }
    }

    /** Devuelve el turno seleccionado en la tabla, o null si no hay selección. */
    private Turno turnoSeleccionado() {
        return modelo.getTurnoEn(tabla.getSelectedRow());
    }

    private void cambiarEstado(String accion) {
        Turno t = turnoSeleccionado();
        if (t == null) return;
        try {
            switch (accion) {
                case "confirmar" -> controladorTurno.confirmar(t.getId());
                case "completar" -> controladorTurno.completar(t.getId());
                case "cancelar"  -> {
                    if (!GuiUtils.confirmar(this, "¿Cancelar el turno de "
                            + t.getPaciente().getNombreCompleto() + "?")) return;
                    controladorTurno.cancelar(t.getId());
                }
            }
            GuiUtils.info(this, "Estado del turno actualizado.");
            refrescar();
        } catch (Exception ex) {
            GuiUtils.error(this, ex.getMessage());
        }
    }

    /** Abre un diálogo con spinners de fecha/hora prellenados para modificar el turno. */
    private void modificar() {
        Turno t = turnoSeleccionado();
        if (t == null) return;

        JSpinner spFecha = GuiUtils.crearSpinnerFecha();
        JSpinner spHora  = GuiUtils.crearSpinnerHora();
        GuiUtils.setFechaSpinner(spFecha, t.getFecha());
        GuiUtils.setHoraSpinner(spHora, t.getHora());

        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.add(new JLabel("Nueva fecha:")); panel.add(spFecha);
        panel.add(new JLabel("Nueva hora:"));  panel.add(spHora);

        int opcion = JOptionPane.showConfirmDialog(this, panel,
                "Modificar turno #" + t.getId() + " — " + t.getPaciente().getNombreCompleto(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;

        try {
            controladorTurno.modificar(t.getId(),
                    GuiUtils.fechaDeSpinner(spFecha), GuiUtils.horaDeSpinner(spHora));
            GuiUtils.info(this, "Turno modificado correctamente.");
            refrescar();
        } catch (Exception ex) {
            GuiUtils.error(this, ex.getMessage());
        }
    }

    /**
     * Habilita cada botón según el estado del turno seleccionado, siguiendo las
     * transiciones válidas: PENDIENTE→CONFIRMADO/CANCELADO, CONFIRMADO→COMPLETADO/CANCELADO.
     */
    private void actualizarBotones() {
        Turno t = turnoSeleccionado();
        EstadoTurno estado = (t != null) ? t.getEstado() : null;

        boolean pendiente  = estado == EstadoTurno.PENDIENTE;
        boolean confirmado = estado == EstadoTurno.CONFIRMADO;

        btnConfirmar.setEnabled(pendiente);
        btnCompletar.setEnabled(confirmado);
        btnCancelar.setEnabled(pendiente || confirmado);
        btnModificar.setEnabled(pendiente || confirmado);
    }
}
