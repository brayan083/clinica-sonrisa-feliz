package clinicasonrisafeliz.presentacion.gui;

import clinicasonrisafeliz.controlador.ControladorOdontologo;
import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.controlador.ControladorTurno;
import clinicasonrisafeliz.modelo.Odontologo;
import clinicasonrisafeliz.modelo.Paciente;
import clinicasonrisafeliz.modelo.Turno;
import clinicasonrisafeliz.presentacion.gui.modelos.ModeloTablaOdontologos;
import clinicasonrisafeliz.presentacion.gui.modelos.ModeloTablaPacientes;
import clinicasonrisafeliz.presentacion.gui.modelos.ModeloTablaTurnos;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

public class PanelBusquedas extends JPanel {

    private final ControladorTurno controladorTurno;
    private final ControladorPaciente controladorPaciente;
    private final ControladorOdontologo controladorOdontologo;

    private JComboBox<String> comboCriterio;
    private JPanel panelInputs;
    private JTextField txtValor1;
    private JTextField txtValor2;
    private JTable tablaResultados;

    private ModeloTablaPacientes modeloPacientes;
    private ModeloTablaTurnos modeloTurnos;
    private ModeloTablaOdontologos modeloOdontologos;

    public PanelBusquedas(ControladorTurno cTurno, ControladorPaciente cPac, ControladorOdontologo cOdon) {
        this.controladorTurno = cTurno;
        this.controladorPaciente = cPac;
        this.controladorOdontologo = cOdon;
        this.modeloPacientes = new ModeloTablaPacientes();
        this.modeloTurnos = new ModeloTablaTurnos();
        this.modeloOdontologos = new ModeloTablaOdontologos();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelCriterios = new JPanel();
        panelCriterios.setLayout(new BoxLayout(panelCriterios, BoxLayout.Y_AXIS));
        panelCriterios.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Criterios de Búsqueda", TitledBorder.LEFT, TitledBorder.TOP));

        comboCriterio = new JComboBox<>(new String[]{
                "Paciente por DNI",
                "Pacientes por Apellido",
                "Odontólogo por Matrícula",
                "Turnos por Fecha",
                "Turnos por Rango de Fechas",
                "Turnos por Paciente (ID)",
                "Turnos por Odontólogo (ID)"
        });
        comboCriterio.addActionListener(e -> cambiarInputs());

        JPanel filaCombo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        filaCombo.add(new JLabel("Buscar:"));
        filaCombo.add(comboCriterio);

        panelInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        txtValor1 = new JTextField(15);
        txtValor2 = new JTextField(15);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> realizarBusqueda());

        JPanel filaInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        filaInputs.add(panelInputs);
        filaInputs.add(btnBuscar);

        panelCriterios.add(filaCombo);
        panelCriterios.add(filaInputs);

        add(panelCriterios, BorderLayout.NORTH);

        tablaResultados = new JTable(modeloTurnos);
        add(new JScrollPane(tablaResultados), BorderLayout.CENTER);

        cambiarInputs();
    }

    private void cambiarInputs() {
        panelInputs.removeAll();
        int idx = comboCriterio.getSelectedIndex();
        switch (idx) {
            case 4: // Rango de fechas
                panelInputs.add(new JLabel("Desde (AAAA-MM-DD):"));
                panelInputs.add(txtValor1);
                panelInputs.add(new JLabel("Hasta (AAAA-MM-DD):"));
                panelInputs.add(txtValor2);
                txtValor2.setVisible(true);
                break;
            default:
                panelInputs.add(new JLabel("Valor:"));
                panelInputs.add(txtValor1);
                txtValor2.setVisible(false);
                break;
        }
        txtValor1.setText("");
        txtValor2.setText("");
        panelInputs.revalidate();
        panelInputs.repaint();
    }

    private void realizarBusqueda() {
        int idx = comboCriterio.getSelectedIndex();
        String val1 = txtValor1.getText().trim();
        String val2 = txtValor2.getText().trim();

        if (val1.isEmpty() && idx != 4) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor para buscar.");
            return;
        }

        try {
            switch (idx) {
                case 0: // Paciente DNI
                    Paciente p = controladorPaciente.buscarPorDni(val1);
                    modeloPacientes.setPacientes(p != null ? List.of(p) : Collections.emptyList());
                    tablaResultados.setModel(modeloPacientes);
                    break;
                case 1: // Paciente Apellido
                    List<Paciente> pacs = controladorPaciente.buscarPorApellido(val1);
                    modeloPacientes.setPacientes(pacs);
                    tablaResultados.setModel(modeloPacientes);
                    break;
                case 2: // Odonto Matrícula
                    Odontologo o = controladorOdontologo.buscarPorMatricula(val1);
                    modeloOdontologos.setOdontologos(o != null ? List.of(o) : Collections.emptyList());
                    tablaResultados.setModel(modeloOdontologos);
                    break;
                case 3: // Turno Fecha
                    LocalDate f = LocalDate.parse(val1);
                    modeloTurnos.setTurnos(controladorTurno.listarPorFecha(f));
                    tablaResultados.setModel(modeloTurnos);
                    break;
                case 4: // Turno Rango
                    LocalDate d = LocalDate.parse(val1);
                    LocalDate h = LocalDate.parse(val2);
                    modeloTurnos.setTurnos(controladorTurno.listarPorRangoDeFechas(d, h));
                    tablaResultados.setModel(modeloTurnos);
                    break;
                case 5: // Turnos Paciente ID
                    modeloTurnos.setTurnos(controladorTurno.listarPorPaciente(Long.parseLong(val1)));
                    tablaResultados.setModel(modeloTurnos);
                    break;
                case 6: // Turnos Odontólogo ID
                    modeloTurnos.setTurnos(controladorTurno.listarPorOdontologo(Long.parseLong(val1)));
                    tablaResultados.setModel(modeloTurnos);
                    break;
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
