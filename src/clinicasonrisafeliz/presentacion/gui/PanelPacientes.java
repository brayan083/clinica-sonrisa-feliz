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

import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.presentacion.gui.tabla.PacienteTableModel;

/**
 * Panel de gestión de pacientes: listado en tabla, formulario de alta/edición
 * y acciones de baja. Se comunica exclusivamente con {@link ControladorPaciente}
 * (capa de servicios), nunca con los repositorios directamente.
 */
public class PanelPacientes extends JPanel {

    private final ControladorPaciente controlador;
    private final PacienteTableModel  modelo = new PacienteTableModel();
    private final JTable              tabla  = new JTable(modelo);

    // Campos del formulario
    private final JTextField txtNombre    = new JTextField(15);
    private final JTextField txtApellido  = new JTextField(15);
    private final JTextField txtEmail     = new JTextField(15);
    private final JTextField txtDni       = new JTextField(15);
    private final JTextField txtCalle     = new JTextField(15);
    private final JTextField txtNumero    = new JTextField(15);
    private final JTextField txtLocalidad = new JTextField(15);
    private final JTextField txtProvincia = new JTextField(15);

    private final JTextField txtBusqueda  = new JTextField(15);

    /** ID del paciente seleccionado; null indica modo "alta de nuevo paciente". */
    private Long idSeleccionado = null;

    public PanelPacientes(ControladorPaciente controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirBarraBusqueda(), BorderLayout.NORTH);
        add(construirTabla(),         BorderLayout.CENTER);
        add(construirFormulario(),    BorderLayout.SOUTH);

        refrescar();
    }

    // ── Construcción de la UI ──────────────────────────────────────────────────

    private JPanel construirBarraBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Búsqueda rápida por apellido"));

        JPanel izq = new JPanel();
        izq.add(new JLabel("Apellido:"));
        izq.add(txtBusqueda);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnTodos  = new JButton("Mostrar todos");
        izq.add(btnBuscar);
        izq.add(btnTodos);

        btnBuscar.addActionListener(e -> buscarPorApellido());
        btnTodos.addActionListener(e -> { txtBusqueda.setText(""); refrescar(); });

        panel.add(izq, BorderLayout.WEST);
        return panel;
    }

    private JScrollPane construirTabla() {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);

        // MouseListener: al hacer clic en una fila, cargamos sus datos en el formulario.
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
        form.setBorder(BorderFactory.createTitledBorder("Datos del paciente"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 6, 4, 6);
        g.anchor = GridBagConstraints.WEST;

        agregarCampo(form, g, 0, "Nombre:",    txtNombre,    "Apellido:",  txtApellido);
        agregarCampo(form, g, 1, "Email:",     txtEmail,     "DNI:",       txtDni);
        agregarCampo(form, g, 2, "Calle:",     txtCalle,     "Número:",    txtNumero);
        agregarCampo(form, g, 3, "Localidad:", txtLocalidad, "Provincia:", txtProvincia);

        // Botonera
        JPanel botones = new JPanel(new GridLayout(1, 4, 8, 0));
        JButton btnNuevo    = new JButton("Nuevo / Limpiar");
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());

        g.gridx = 0; g.gridy = 4; g.gridwidth = 4; g.fill = GridBagConstraints.HORIZONTAL;
        form.add(botones, g);

        return form;
    }

    /** Agrega una fila con dos pares etiqueta/campo al formulario. */
    private void agregarCampo(JPanel form, GridBagConstraints g, int fila,
                              String etq1, JTextField campo1, String etq2, JTextField campo2) {
        g.gridwidth = 1; g.fill = GridBagConstraints.NONE;
        g.gridy = fila;
        g.gridx = 0; form.add(new JLabel(etq1), g);
        g.gridx = 1; form.add(campo1, g);
        g.gridx = 2; form.add(new JLabel(etq2), g);
        g.gridx = 3; form.add(campo2, g);
    }

    // ── Acciones ────────────────────────────────────────────────────────────────

    /** Recarga la tabla con todos los pacientes. */
    public void refrescar() {
        modelo.setPacientes(controlador.listarTodos());
    }

    private void buscarPorApellido() {
        String apellido = txtBusqueda.getText();
        if (apellido == null || apellido.isBlank()) {
            refrescar();
            return;
        }
        try {
            modelo.setPacientes(controlador.buscarPorApellido(apellido));
        } catch (Exception ex) {
            GuiUtils.error(this, ex.getMessage());
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        Paciente p = modelo.getPacienteEn(fila);
        if (p == null) return;

        idSeleccionado = p.getId();
        txtNombre.setText(p.getNombre());
        txtApellido.setText(p.getApellido());
        txtEmail.setText(p.getEmail());
        txtDni.setText(p.getDni());

        Domicilio d = p.getDomicilio();
        txtCalle.setText(d != null ? d.getCalle() : "");
        txtNumero.setText(d != null ? d.getNumero() : "");
        txtLocalidad.setText(d != null ? d.getLocalidad() : "");
        txtProvincia.setText(d != null ? d.getProvincia() : "");

        // Al editar un paciente existente solo se modifican nombre, apellido y email
        // (el servicio no expone cambio de DNI ni de domicilio). Bloqueamos esos campos.
        setEdicionDomicilioHabilitada(false);
        limpiarBordes();
    }

    private void guardar() {
        if (!validarFormulario()) {
            GuiUtils.error(this, "Revisá los campos marcados en rojo antes de guardar.");
            return;
        }
        try {
            if (idSeleccionado == null) {
                // Alta de un nuevo paciente
                Domicilio domicilio = new Domicilio(
                        txtCalle.getText().trim(), txtNumero.getText().trim(),
                        txtLocalidad.getText().trim(), txtProvincia.getText().trim());
                Paciente nuevo = controlador.registrar(
                        txtNombre.getText().trim(), txtApellido.getText().trim(),
                        txtEmail.getText().trim(), txtDni.getText().trim(), domicilio);
                GuiUtils.info(this, "Paciente registrado correctamente (ID " + nuevo.getId() + ").");
            } else {
                // Modificación de un paciente existente
                controlador.actualizar(idSeleccionado,
                        txtNombre.getText().trim(), txtApellido.getText().trim(),
                        txtEmail.getText().trim());
                GuiUtils.info(this, "Paciente actualizado correctamente.");
            }
            limpiarFormulario();
            refrescar();
        } catch (Exception ex) {
            GuiUtils.error(this, ex.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            GuiUtils.advertencia(this, "Seleccioná un paciente de la tabla para eliminarlo.");
            return;
        }
        if (!GuiUtils.confirmar(this, "¿Eliminar al paciente seleccionado?\nEsta acción no se puede deshacer.")) {
            return;
        }
        try {
            controlador.eliminar(idSeleccionado);
            GuiUtils.info(this, "Paciente eliminado.");
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
            // DNI y domicilio solo se validan al dar de alta
            ok &= GuiUtils.validarNoVacio(txtDni);
            ok &= GuiUtils.validarNoVacio(txtCalle);
            ok &= GuiUtils.validarNoVacio(txtNumero);
            ok &= GuiUtils.validarNoVacio(txtLocalidad);
            ok &= GuiUtils.validarNoVacio(txtProvincia);
        }
        return ok;
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        tabla.clearSelection();
        for (JTextField c : new JTextField[]{txtNombre, txtApellido, txtEmail, txtDni,
                txtCalle, txtNumero, txtLocalidad, txtProvincia}) {
            c.setText("");
        }
        setEdicionDomicilioHabilitada(true);
        limpiarBordes();
    }

    private void setEdicionDomicilioHabilitada(boolean habilitado) {
        txtDni.setEnabled(habilitado);
        txtCalle.setEnabled(habilitado);
        txtNumero.setEnabled(habilitado);
        txtLocalidad.setEnabled(habilitado);
        txtProvincia.setEnabled(habilitado);
    }

    private void limpiarBordes() {
        for (JTextField c : new JTextField[]{txtNombre, txtApellido, txtEmail, txtDni,
                txtCalle, txtNumero, txtLocalidad, txtProvincia}) {
            GuiUtils.limpiarBorde(c);
        }
    }
}
