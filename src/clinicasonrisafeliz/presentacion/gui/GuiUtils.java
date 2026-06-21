package clinicasonrisafeliz.presentacion.gui;

import java.awt.Color;
import java.awt.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

/**
 * Utilidades transversales para la interfaz gráfica.
 *
 * Centraliza el manejo de diálogos ({@link JOptionPane}), la validación
 * visual de campos (bordes rojos) y el parseo de fechas/horas, de modo que
 * los paneles no repitan este código y se mantengan enfocados en su layout.
 */
public final class GuiUtils {

    /** Formato de fecha aceptado en los formularios. */
    public static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    /** Formato de hora aceptado en los formularios. */
    public static final DateTimeFormatter FMT_HORA  = DateTimeFormatter.ofPattern("HH:mm");

    /** Borde rojo aplicado a un campo cuando su contenido es inválido. */
    private static final Border BORDE_ERROR  = BorderFactory.createLineBorder(Color.RED, 2);
    /** Borde por defecto, restaurado cuando el campo vuelve a ser válido. */
    private static final Border BORDE_NORMAL = new javax.swing.JTextField().getBorder();

    private GuiUtils() { } // clase de utilidades: no se instancia

    // ── Diálogos ────────────────────────────────────────────────────────────

    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void advertencia(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    /** Diálogo Sí/No. Devuelve true solo si el usuario confirma. */
    public static boolean confirmar(Component padre, String mensaje) {
        int r = JOptionPane.showConfirmDialog(padre, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return r == JOptionPane.YES_OPTION;
    }

    // ── Validación visual de campos ───────────────────────────────────────────

    /** Pinta el borde del campo de rojo para señalar un error de validación. */
    public static void marcarInvalido(JComponent campo) {
        campo.setBorder(BORDE_ERROR);
    }

    /** Restaura el borde por defecto del campo. */
    public static void limpiarBorde(JComponent campo) {
        campo.setBorder(BORDE_NORMAL);
    }

    /**
     * Valida que un campo de texto no esté vacío.
     * Si lo está, lo marca en rojo y devuelve false; si es válido, limpia el borde.
     */
    public static boolean validarNoVacio(JTextComponent campo) {
        if (campo.getText() == null || campo.getText().isBlank()) {
            marcarInvalido(campo);
            return false;
        }
        limpiarBorde(campo);
        return true;
    }

    /**
     * Valida que el campo contenga un email con formato mínimo (contiene '@').
     */
    public static boolean validarEmail(JTextComponent campo) {
        String v = campo.getText();
        if (v == null || v.isBlank() || !v.contains("@")) {
            marcarInvalido(campo);
            return false;
        }
        limpiarBorde(campo);
        return true;
    }

    // ── Parseo de fecha / hora ─────────────────────────────────────────────────

    /**
     * Intenta parsear una fecha en formato dd/MM/yyyy.
     * @return la fecha, o null si el texto no tiene el formato esperado.
     */
    public static LocalDate parsearFecha(String texto) {
        try {
            return LocalDate.parse(texto.trim(), FMT_FECHA);
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Intenta parsear una hora en formato HH:mm.
     * @return la hora, o null si el texto no tiene el formato esperado.
     */
    public static LocalTime parsearHora(String texto) {
        try {
            return LocalTime.parse(texto.trim(), FMT_HORA);
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    // ── Spinners de fecha y hora ───────────────────────────────────────────────
    // Usan SpinnerDateModel y un editor con formato fijo, de modo que el usuario
    // no pueda escribir una fecha/hora con formato inválido (mejor UX que un
    // campo de texto libre que después hay que parsear y validar).

    /** Crea un spinner de fecha (dd/MM/aaaa) posicionado en el día de hoy. */
    public static JSpinner crearSpinnerFecha() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        return spinner;
    }

    /** Crea un spinner de hora (HH:mm). */
    public static JSpinner crearSpinnerHora() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
        return spinner;
    }

    /** Lee la fecha seleccionada en un spinner como {@link LocalDate}. */
    public static LocalDate fechaDeSpinner(JSpinner spinner) {
        Date d = (Date) spinner.getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /** Lee la hora seleccionada en un spinner como {@link LocalTime} (sin segundos). */
    public static LocalTime horaDeSpinner(JSpinner spinner) {
        Date d = (Date) spinner.getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().withSecond(0).withNano(0);
    }

    /** Posiciona un spinner de fecha en la fecha indicada. */
    public static void setFechaSpinner(JSpinner spinner, LocalDate fecha) {
        spinner.setValue(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    /** Posiciona un spinner de hora en la hora indicada. */
    public static void setHoraSpinner(JSpinner spinner, LocalTime hora) {
        spinner.setValue(Date.from(LocalDate.now().atTime(hora).atZone(ZoneId.systemDefault()).toInstant()));
    }
}
