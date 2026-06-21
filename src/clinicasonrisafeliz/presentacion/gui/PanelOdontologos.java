package clinicasonrisafeliz.presentacion.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.presentacion.gui.tabla.OdontologoTableModel;

/**
 * Panel de gestión de odontólogos: listado en tabla, alta/edición y baja.
 * Comparte la estructura de {@link PanelPacientes} y delega en
 * {@link ControladorOdontologo}.
 */
public class PanelOdontologos extends JPanel {

    private final ControladorOdontologo controlador;
    private final OdontologoTableModel  modelo = new OdontologoTableModel();
    private final JTable                tabla  = new JTable(modelo);

    private final JTextField txtNombre    = new JTextField(15);
    private final JTextField txtApellido  = new JTextField(15);
    private final JTextField txtEmail     = new JTextField(15);
    private final JTextField txtMatricula = new JTextField(15);

    /** ID del odontólogo seleccionado; null indica modo "alta". */
    private Long idSeleccionado = null;

    public PanelOdontologos(ControladorOdontologo controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirTabla(),      BorderLayout.CENTER);
        add(construirFormulario(), BorderLayout.SOUTH);

        refrescar();
    }

    private JScrollPane construirTabla() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarSeleccionEnFormulario();
            }
        });
        return new JScrollPane(tabla);
    }

    private JPanel construirFormulario() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del odontólogo"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        g.gridy = 0;
        g.gridx = 0; form.add(new JLabel("Nombre:"),   g);
        g.gridx = 1; form.add(txtNombre,   g);
        g.gridx = 2; form.add(new JLabel("Apellido:"), g);
        g.gridx = 3; form.add(txtApellido, g);

        g.gridy = 1;
        g.gridx = 0; form.add(new JLabel("Email:"),     g);
        g.gridx = 1; form.add(txtEmail,     g);
        g.gridx = 2; form.add(new JLabel("Matrícula:"), g);
        g.gridx = 3; form.add(txtMatricula, g);

        JPanel botones = new JPanel(new GridLayout(1, 3, 8, 0));
        JButton btnNuevo    = new JButton("Nuevo / Limpiar");
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());

        g.gridx = 0; g.gridy = 2; g.gridwidth = 4; g.fill = GridBagConstraints.HORIZONTAL;
        form.add(botones, g);

        return form;
    }

    // ── Acciones ────────────────────────────────────────────────────────────────

    public void refrescar() {
        modelo.setOdontologos(controlador.listarTodos());
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        Odontologo o = modelo.getOdontologoEn(fila);
        if (o == null) return;

        idSeleccionado = o.getId();
        txtNombre.setText(o.getNombre());
        txtApellido.setText(o.getApellido());
        txtEmail.setText(o.getEmail());
        txtMatricula.setText(o.getMatricula());

        // La matrícula no se modifica en una edición (el servicio no lo permite).
        txtMatricula.setEnabled(false);
        limpiarBordes();
    }

    private void guardar() {
        if (!validarFormulario()) {
            GuiUtils.error(this, "Revisá los campos marcados en rojo antes de guardar.");
            return;
        }
        try {
            if (idSeleccionado == null) {
                Odontologo nuevo = controlador.registrar(
                        txtNombre.getText().trim(), txtApellido.getText().trim(),
                        txtEmail.getText().trim(), txtMatricula.getText().trim());
                GuiUtils.info(this, "Odontólogo registrado correctamente (ID " + nuevo.getId() + ").");
            } else {
                controlador.actualizar(idSeleccionado,
                        txtNombre.getText().trim(), txtApellido.getText().trim(),
                        txtEmail.getText().trim());
                GuiUtils.info(this, "Odontólogo actualizado correctamente.");
            }
            limpiarFormulario();
            refrescar();
        } catch (Exception ex) {
            GuiUtils.error(this, ex.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            GuiUtils.advertencia(this, "Seleccioná un odontólogo de la tabla para eliminarlo.");
            return;
        }
        if (!GuiUtils.confirmar(this, "¿Eliminar al odontólogo seleccionado?\nEsta acción no se puede deshacer.")) {
            return;
        }
        try {
            controlador.eliminar(idSeleccionado);
            GuiUtils.info(this, "Odontólogo eliminado.");
            limpiarFormulario();
            refrescar();
        } catch (Exception ex) {
            GuiUtils.error(this, ex.getMessage());
        }
    }

    // ── Validación y limpieza ─────────────────────────────────────────────────

    private boolean validarFormulario() {
        boolean ok = true;
        ok &= GuiUtils.validarNoVacio(txtNombre);
        ok &= GuiUtils.validarNoVacio(txtApellido);
        ok &= GuiUtils.validarEmail(txtEmail);
        if (idSeleccionado == null) {
            ok &= GuiUtils.validarNoVacio(txtMatricula);
        }
        return ok;
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        tabla.clearSelection();
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtMatricula.setText("");
        txtMatricula.setEnabled(true);
        limpiarBordes();
    }

    private void limpiarBordes() {
        for (JTextField c : new JTextField[]{txtNombre, txtApellido, txtEmail, txtMatricula}) {
            GuiUtils.limpiarBorde(c);
        }
    }
}
