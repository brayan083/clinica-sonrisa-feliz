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

    /** Constructor usado al cargar desde CSV; no incrementa el contador global. */
    protected Persona(long id, String nombre, String apellido, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public static void resetContador(long nextId) {
        contadorId = nextId;
    }

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
        return "ID: " + id + " | Nombre: " + getNombreCompleto() + " | Email: " + email;
    }
}
