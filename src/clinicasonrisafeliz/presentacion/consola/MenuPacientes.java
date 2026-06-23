package clinicasonrisafeliz.presentacion.consola;

import clinicasonrisafeliz.controlador.ControladorPaciente;
import clinicasonrisafeliz.modelo.Domicilio;
import clinicasonrisafeliz.modelo.Paciente;

import java.util.List;

public class MenuPacientes {

    private final ControladorPaciente controladorPaciente;
    private final ConsolaUtils        utils;

    public MenuPacientes(ControladorPaciente controladorPaciente, ConsolaUtils utils) {
        this.controladorPaciente = controladorPaciente;
        this.utils               = utils;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n========== GESTIÓN DE PACIENTES ==========");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar por DNI");
            System.out.println("4. Buscar por apellido");
            System.out.println("5. Listar todos (detallado)");
            System.out.println("6. Listar nombres (compacto)");
            System.out.println("7. Modificar paciente");
            System.out.println("8. Eliminar paciente");
            System.out.println("0. Volver");
            opcion = utils.leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> registrar();
                case 2 -> buscarPorId();
                case 3 -> buscarPorDni();
                case 4 -> buscarPorApellido();
                case 5 -> listarTodos();
                case 6 -> listarNombresCompletos();
                case 7 -> modificar();
                case 8 -> eliminar();
                case 0 -> System.out.println("Volviendo al menú anterior...");
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    public boolean hayPacientes() {
        return !controladorPaciente.listarTodos().isEmpty();
    }

    public void listarTodos() {
        List<Paciente> lista = controladorPaciente.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        System.out.println("\n--- Pacientes (orden alfabético) ---");
        lista.forEach(p -> System.out.println("  [" + p.getId() + "] " + p.getNombreCompleto() + " - DNI: " + p.getDni()));
    }

    private void listarNombresCompletos() {
        List<String> nombres = controladorPaciente.listarNombresCompletos();
        if (nombres.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        System.out.println("\n--- Pacientes (listado compacto) ---");
        nombres.forEach(n -> System.out.println("  " + n));
    }

    private void registrar() {
        System.out.println("\n--- Registrar Paciente ---");
        try {
            String nombre    = utils.leerTexto("Nombre: ");
            String apellido  = utils.leerTexto("Apellido: ");
            String email     = utils.leerEmail("Email: ");
            String dni       = utils.leerTexto("DNI: ");
            System.out.println("  [Domicilio]");
            String calle     = utils.leerTexto("  Calle: ");
            String numero    = utils.leerTexto("  Número: ");
            String localidad = utils.leerTexto("  Localidad: ");
            String provincia = utils.leerTexto("  Provincia: ");
            Domicilio domicilio = new Domicilio(calle, numero, localidad, provincia);
            Paciente p = controladorPaciente.registrar(nombre, apellido, email, dni, domicilio);
            System.out.println("✓ Paciente registrado con ID " + p.getId() + ": " + p.getNombreCompleto());
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void buscarPorId() {
        Long id = utils.leerIdExistente("ID del paciente: ", controladorPaciente::buscarPorId);
        System.out.println(controladorPaciente.buscarPorId(id));
    }

    private void buscarPorDni() {
        try {
            String dni = utils.leerTexto("DNI: ");
            System.out.println(controladorPaciente.buscarPorDni(dni));
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void buscarPorApellido() {
        String apellido = utils.leerTexto("Apellido (o parte): ");
        List<Paciente> resultados = controladorPaciente.buscarPorApellido(apellido);
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron pacientes con ese apellido.");
        } else {
            System.out.println("\n--- Resultados ---");
            resultados.forEach(p ->
                System.out.println("  [" + p.getId() + "] " + p.getNombreCompleto() + " - DNI: " + p.getDni()));
        }
    }

    private void modificar() {
        listarTodos();
        Long id = utils.leerIdExistente("ID del paciente a modificar: ", controladorPaciente::buscarPorId);
        Paciente p = controladorPaciente.buscarPorId(id);
        System.out.println("Datos actuales: " + p);
        System.out.println("(Deje en blanco para no modificar)");
        String nombre   = utils.leerTextoOpcional("Nuevo nombre [" + p.getNombre() + "]: ", p.getNombre());
        String apellido = utils.leerTextoOpcional("Nuevo apellido [" + p.getApellido() + "]: ", p.getApellido());
        String email    = utils.leerEmailOpcional("Nuevo email [" + p.getEmail() + "]: ", p.getEmail());
        try {
            Domicilio dom = p.getDomicilio();
            String calle    = dom != null ? dom.getCalle()     : "";
            String numero   = dom != null ? dom.getNumero()    : "";
            String localidad = dom != null ? dom.getLocalidad() : "";
            String provincia = dom != null ? dom.getProvincia() : "";
            controladorPaciente.actualizar(id, nombre, apellido, email, calle, numero, localidad, provincia);
            System.out.println("✓ Paciente actualizado.");
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void eliminar() {
        listarTodos();
        Long id = utils.leerIdExistente("ID del paciente a eliminar: ", controladorPaciente::buscarPorId);
        Paciente p = controladorPaciente.buscarPorId(id);
        System.out.println("Paciente a eliminar: [" + p.getId() + "] " + p.getNombreCompleto() + " - DNI: " + p.getDni());
        if (!utils.confirmar("¿Está seguro que desea eliminar este paciente?")) {
            System.out.println("Operación cancelada.");
            return;
        }
        try {
            controladorPaciente.eliminar(id);
            System.out.println("✓ Paciente eliminado.");
        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
        }
    }
}
