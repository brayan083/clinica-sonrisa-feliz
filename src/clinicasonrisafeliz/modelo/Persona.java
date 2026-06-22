package clinicasonrisafeliz.modelo;

public abstract class Persona {
    private static long contadorId = 1;

    private final Long id;
    private String nombre;
    private String apellido;
    private String email;

    public Persona(String nombre, String apellido, String email) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    /** Constructor usado al cargar desde CSV; ajusta el contador para evitar colisiones. */
    protected Persona(long id, String nombre, String apellido, String email) {
        this.id = id;
        if (id >= contadorId) contadorId = id + 1;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    /**
     * Devuelve el nombre del rol de esta persona en la clínica.
     * Cada subclase lo implementa con su propio valor.
     */
    public abstract String getRol();

    /**
     * Devuelve el identificador único del rol (DNI, matrícula o legajo).
     * Cada subclase lo implementa con su campo específico.
     */
    public abstract String getIdentificador();

    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return getRol() + " | " + getNombreCompleto() + " | " + getIdentificador() + " | Email: " + email;
    }
}
