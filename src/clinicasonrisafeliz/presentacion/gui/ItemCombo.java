package clinicasonrisafeliz.presentacion.gui;

/**
 * Envoltorio genérico para usar entidades del dominio dentro de un
 * {@link javax.swing.JComboBox}.
 *
 * Asocia una etiqueta legible (lo que ve el usuario) con el objeto real
 * (lo que necesita el programa), de modo que el combo muestre, por ejemplo,
 * "Juan García" pero al seleccionarlo devuelva el {@code Paciente} completo.
 *
 * @param <T> tipo de la entidad envuelta.
 */
public class ItemCombo<T> {

    private final String etiqueta;
    private final T      valor;

    public ItemCombo(String etiqueta, T valor) {
        this.etiqueta = etiqueta;
        this.valor    = valor;
    }

    public T getValor() { return valor; }

    @Override
    public String toString() { return etiqueta; } // lo que se muestra en el combo
}
