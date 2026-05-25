package clinicasonrisafeliz.modelo;

import java.util.Objects;

public class Recepcionista extends Persona {
    private String legajo;

    public Recepcionista(String nombre, String apellido, String email, String legajo) {
        super(nombre, apellido, email);
        this.legajo = legajo;
    }

    public Recepcionista(long id, String nombre, String apellido, String email, String legajo) {
        super(id, nombre, apellido, email);
        this.legajo = legajo;
    }

    public String getLegajo() { return legajo; }
    public void setLegajo(String legajo) { this.legajo = legajo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recepcionista that = (Recepcionista) o;
        return Objects.equals(legajo, that.legajo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(legajo);
    }

    @Override
    public String toString() {
        return "Recepcionista{" +
                "nombre='" + getNombreCompleto() + '\'' +
                ", legajo='" + legajo + '\'' +
                '}';
    }
}
