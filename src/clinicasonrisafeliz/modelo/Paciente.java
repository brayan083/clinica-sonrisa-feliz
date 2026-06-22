package clinicasonrisafeliz.modelo;

import java.time.LocalDate;
import java.util.Objects;

public class Paciente extends Persona implements Comparable<Paciente> {
    private String dni;
    private LocalDate fechaIngreso;
    private Domicilio domicilio;

    public Paciente(String nombre, String apellido, String email, String dni, Domicilio domicilio) {
        super(nombre, apellido, email);
        this.dni = dni;
        this.domicilio = domicilio;
        this.fechaIngreso = LocalDate.now();
    }

    /** Constructor usado al cargar desde CSV. */
    public Paciente(long id, String nombre, String apellido, String email, String dni,
                    LocalDate fechaIngreso, Domicilio domicilio) {
        super(id, nombre, apellido, email);
        this.dni = dni;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
    }

    @Override
    public String getRol() { return "Paciente"; }

    @Override
    public String getIdentificador() { return "DNI: " + dni; }

    @Override
    public int compareTo(Paciente otro) {
        int cmp = this.getApellido().compareToIgnoreCase(otro.getApellido());
        if (cmp != 0) return cmp;
        return this.getNombre().compareToIgnoreCase(otro.getNombre());
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public Domicilio getDomicilio() { return domicilio; }
    public void setDomicilio(Domicilio domicilio) { this.domicilio = domicilio; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(dni, paciente.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "nombreCompleto='" + getNombreCompleto() + '\'' +
                ", dni='" + dni + '\'' +
                ", domicilio=" + (domicilio != null ? domicilio.toString() : "Sin Domicilio") +
                '}';
    }
}
