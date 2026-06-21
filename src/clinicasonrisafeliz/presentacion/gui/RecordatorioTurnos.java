package clinicasonrisafeliz.presentacion.gui;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import clinicasonrisafeliz.controlador.ControladorTurno;
import clinicasonrisafeliz.enums.EstadoTurno;
import clinicasonrisafeliz.modelo.Turno;

/**
 * Tarea en segundo plano (hilo) que detecta turnos próximos y notifica a la
 * interfaz para recordárselos al usuario.
 *
 * Implementa {@link Runnable} y corre en un hilo demonio, de modo que no
 * impide el cierre de la aplicación. Cada cierto intervalo recalcula los
 * turnos dentro de los próximos {@value #DIAS_ANTICIPACION} días que aún no
 * fueron cancelados ni completados, y entrega el mensaje a un callback que
 * la GUI ejecuta de forma segura en el Event Dispatch Thread.
 */
public class RecordatorioTurnos implements Runnable {

    /** Ventana de anticipación: turnos dentro de los próximos N días. */
    private static final int  DIAS_ANTICIPACION = 2;
    /** Cada cuánto se recalculan los recordatorios (milisegundos). */
    private static final long INTERVALO_MS      = 5 * 60 * 1000; // 5 minutos
    /** Pausa inicial antes del primer chequeo, para no solaparse con el arranque. */
    private static final long PAUSA_INICIAL_MS  = 3 * 1000;      // 3 segundos

    private final ControladorTurno controlador;
    private final Consumer<String> notificador;
    private volatile boolean activo = true;

    /**
     * @param controlador fuente de turnos.
     * @param notificador callback que recibe el mensaje del recordatorio
     *                    (se invoca siempre dentro del EDT).
     */
    public RecordatorioTurnos(ControladorTurno controlador, Consumer<String> notificador) {
        this.controlador = controlador;
        this.notificador = notificador;
    }

    /** Lanza la tarea en un hilo demonio y lo devuelve por si se quiere referenciar. */
    public Thread iniciar() {
        Thread hilo = new Thread(this, "recordatorio-turnos");
        hilo.setDaemon(true); // no impide cerrar la aplicación
        hilo.start();
        return hilo;
    }

    /** Detiene el bucle de forma ordenada. */
    public void detener() {
        activo = false;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(PAUSA_INICIAL_MS);
            while (activo) {
                String mensaje = construirMensaje();
                if (mensaje != null) {
                    // Las actualizaciones de UI deben ocurrir en el EDT.
                    SwingUtilities.invokeLater(() -> notificador.accept(mensaje));
                }
                Thread.sleep(INTERVALO_MS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restaura el flag y termina
        }
    }

    /** Devuelve el texto del recordatorio, o null si no hay turnos próximos. */
    private String construirMensaje() {
        LocalDate hoy   = LocalDate.now();
        LocalDate limite = hoy.plusDays(DIAS_ANTICIPACION);

        List<Turno> proximos = controlador.listarTodos().stream()
                .filter(t -> t.getEstado() == EstadoTurno.PENDIENTE
                          || t.getEstado() == EstadoTurno.CONFIRMADO)
                .filter(t -> !t.getFecha().isBefore(hoy) && !t.getFecha().isAfter(limite))
                .sorted()
                .collect(Collectors.toList());

        if (proximos.isEmpty()) {
            return null;
        }
        return "Recordatorio: hay " + proximos.size()
                + " turno(s) en los próximos " + DIAS_ANTICIPACION + " días. "
                + "Próximo: " + proximos.get(0).getPaciente().getNombreCompleto()
                + " el " + proximos.get(0).getFecha().format(GuiUtils.FMT_FECHA)
                + " a las " + proximos.get(0).getHora().format(GuiUtils.FMT_HORA) + ".";
    }
}
