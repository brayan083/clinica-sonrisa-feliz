package clinicasonrisafeliz.presentacion.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.controlador.ControladorTurno;
import clinicasonrisafeliz.enums.EstadoTurno;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Turno;

/**
 * Panel de búsquedas avanzadas. Permite consultar pacientes, odontólogos y
 * turnos por distintos criterios y muestra el resultado en una {@link JTable}.
 *
 * A diferencia de los demás paneles (que usan AbstractTableModel), aquí se usa
 * {@link DefaultTableModel} porque las columnas cambian según el tipo de
 * búsqueda; así el proyecto demuestra ambos enfoques de modelo de tabla.
 *
 * La zona de criterios se arma en dos filas de altura fija y el área de entrada
 * cambia según el tipo de búsqueda (campo de texto, combo de estados o spinners
 * de fecha), de modo que ningún control quede oculto.
 */
public class PanelBusquedas extends JPanel {

    private static final String BUS_PAC_DNI      = "Paciente por DNI";
    private static final String BUS_PAC_APELLIDO = "Pacientes por apellido";
    private static final String BUS_ODO_MAT      = "Odontólogo por matrícula";
    private static final String BUS_TUR_ESTADO   = "Turnos por estado";
    private static final String BUS_TUR_RANGO    = "Turnos por rango de fechas";

    private final ControladorPaciente   controladorPaciente;
    private final ControladorOdontologo controladorOdontologo;
    private final ControladorTurno      controladorTurno;

    private final JComboBox<String> comboTipo = new JComboBox<>(new String[]{
            BUS_PAC_DNI, BUS_PAC_APELLIDO, BUS_ODO_MAT, BUS_TUR_ESTADO, BUS_TUR_RANGO});

    // Controles de criterio (se muestran según el tipo de búsqueda)
    private final JTextField                txtCriterio = new JTextField(15);
    private final JComboBox<EstadoTurno>    comboEstado = new JComboBox<>(EstadoTurno.values());
    private final JSpinner                  spDesde     = GuiUtils.crearSpinnerFecha();
    private final JSpinner                  spHasta     = GuiUtils.crearSpinnerFecha();

    /** Fila que aloja los controles de criterio; se reconstruye al cambiar el tipo. */
    private final JPanel filaCriterio = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));

    private final DefaultTableModel modeloResultados =
            new DefaultTableModel() {
                @Override public boolean isCellEditable(int f, int c) { return false; }
            };
    private final JTable tablaResultados = new JTable(modeloResultados);

    public PanelBusquedas(ControladorPaciente controladorPaciente,
                          ControladorOdontologo controladorOdontologo,
                          ControladorTurno controladorTurno) {
        this.controladorPaciente   = controladorPaciente;
        this.controladorOdontologo = controladorOdontologo;
        this.controladorTurno      = controladorTurno;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirControles(), BorderLayout.NORTH);
        tablaResultados.setFillsViewportHeight(true);
        tablaResultados.setRowHeight(22);
        add(new JScrollPane(tablaResultados), BorderLayout.CENTER);

        comboTipo.addActionListener(e -> actualizarCriterio());
        actualizarCriterio();
    }

    private JPanel construirControles() {
        // Dos filas de altura fija: evita que un control se oculte al envolver.
        JPanel contenedor = new JPanel(new GridLayout(2, 1, 0, 4));
        contenedor.setBorder(BorderFactory.createTitledBorder("Criterios de búsqueda"));

        JPanel filaTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filaTipo.add(new JLabel("Buscar:"));
        filaTipo.add(comboTipo);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> ejecutarBusqueda());
        filaTipo.add(btnBuscar);

        contenedor.add(filaTipo);
        contenedor.add(filaCriterio);
        return contenedor;
    }

    /** Reconstruye la fila de criterio con los controles adecuados al tipo elegido. */
    private void actualizarCriterio() {
        String tipo = (String) comboTipo.getSelectedItem();
        txtCriterio.setText("");
        filaCriterio.removeAll();

        switch (tipo) {
            case BUS_PAC_DNI      -> { filaCriterio.add(new JLabel("DNI:"));       filaCriterio.add(txtCriterio); }
            case BUS_PAC_APELLIDO -> { filaCriterio.add(new JLabel("Apellido:"));  filaCriterio.add(txtCriterio); }
            case BUS_ODO_MAT      -> { filaCriterio.add(new JLabel("Matrícula:")); filaCriterio.add(txtCriterio); }
            case BUS_TUR_ESTADO   -> { filaCriterio.add(new JLabel("Estado:"));    filaCriterio.add(comboEstado); }
            case BUS_TUR_RANGO    -> {
                filaCriterio.add(new JLabel("Desde:")); filaCriterio.add(spDesde);
                filaCriterio.add(new JLabel("Hasta:")); filaCriterio.add(spHasta);
            }
        }
        filaCriterio.revalidate();
        filaCriterio.repaint();
    }

    // ── Ejecución de la búsqueda ───────────────────────────────────────────────

    private void ejecutarBusqueda() {
        String tipo = (String) comboTipo.getSelectedItem();
        try {
            switch (tipo) {
                case BUS_PAC_DNI      -> buscarPacientePorDni();
                case BUS_PAC_APELLIDO -> buscarPacientesPorApellido();
                case BUS_ODO_MAT      -> buscarOdontologoPorMatricula();
                case BUS_TUR_ESTADO   -> buscarTurnosPorEstado();
                case BUS_TUR_RANGO    -> buscarTurnosPorRango();
            }
        } catch (Exception ex) {
            GuiUtils.error(this, ex.getMessage());
        }
    }

    private void buscarPacientePorDni() {
        if (!GuiUtils.validarNoVacio(txtCriterio)) { GuiUtils.error(this, "Ingresá un DNI."); return; }
        Paciente p = controladorPaciente.buscarPorDni(txtCriterio.getText().trim());
        mostrarPacientes(List.of(p));
    }

    private void buscarPacientesPorApellido() {
        if (!GuiUtils.validarNoVacio(txtCriterio)) { GuiUtils.error(this, "Ingresá un apellido."); return; }
        mostrarPacientes(controladorPaciente.buscarPorApellido(txtCriterio.getText().trim()));
    }

    private void buscarOdontologoPorMatricula() {
        if (!GuiUtils.validarNoVacio(txtCriterio)) { GuiUtils.error(this, "Ingresá una matrícula."); return; }
        Odontologo o = controladorOdontologo.buscarPorMatricula(txtCriterio.getText().trim());
        mostrarOdontologos(List.of(o));
    }

    private void buscarTurnosPorEstado() {
        EstadoTurno estado = (EstadoTurno) comboEstado.getSelectedItem();
        mostrarTurnos(controladorTurno.listarPorEstado(estado));
    }

    private void buscarTurnosPorRango() {
        LocalDate desde = GuiUtils.fechaDeSpinner(spDesde);
        LocalDate hasta = GuiUtils.fechaDeSpinner(spHasta);
        mostrarTurnos(controladorTurno.listarPorRangoDeFechas(desde, hasta));
    }

    // ── Volcado de resultados a la tabla ───────────────────────────────────────

    private void mostrarPacientes(List<Paciente> lista) {
        modeloResultados.setDataVector(new Object[0][], new Object[]{"ID", "Nombre", "Apellido", "DNI", "Email", "Domicilio"});
        for (Paciente p : lista) {
            modeloResultados.addRow(new Object[]{p.getId(), p.getNombre(), p.getApellido(), p.getDni(),
                    p.getEmail(), p.getDomicilio() != null ? p.getDomicilio().toString() : "—"});
        }
        avisarSiVacio(lista.size());
    }

    private void mostrarOdontologos(List<Odontologo> lista) {
        modeloResultados.setDataVector(new Object[0][], new Object[]{"ID", "Nombre", "Apellido", "Matrícula", "Email"});
        for (Odontologo o : lista) {
            modeloResultados.addRow(new Object[]{o.getId(), o.getNombre(), o.getApellido(), o.getMatricula(), o.getEmail()});
        }
        avisarSiVacio(lista.size());
    }

    private void mostrarTurnos(List<Turno> lista) {
        modeloResultados.setDataVector(new Object[0][],
                new Object[]{"ID", "Fecha", "Hora", "Paciente", "Odontólogo", "Estado"});
        for (Turno t : lista) {
            modeloResultados.addRow(new Object[]{t.getId(), t.getFecha().format(GuiUtils.FMT_FECHA),
                    t.getHora().format(GuiUtils.FMT_HORA), t.getPaciente().getNombreCompleto(),
                    t.getOdontologo().getNombreCompleto(), t.getEstado()});
        }
        avisarSiVacio(lista.size());
    }

    private void avisarSiVacio(int cantidad) {
        if (cantidad == 0) {
            GuiUtils.info(this, "La búsqueda no arrojó resultados.");
        }
    }
}
