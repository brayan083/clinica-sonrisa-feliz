package clinicasonrisafeliz.presentacion.consola;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsolaUtils {

    private final Scanner scanner;

    public ConsolaUtils(Scanner scanner) {
        this.scanner = scanner;
    }

    public String leerTexto(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("  El campo no puede estar vacío.");
        }
    }

    public String leerTextoOpcional(String prompt, String valorActual) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? valorActual : input;
    }

    public int leerEntero(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public Long leerLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Ingrese un número válido.");
            }
        }
    }

    public boolean confirmar(String prompt) {
        while (true) {
            System.out.print(prompt + " (s/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("s")) return true;
            if (input.equals("n")) return false;
            System.out.println("  Ingrese 's' para confirmar o 'n' para cancelar.");
        }
    }

    public LocalDate leerFecha(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Use AAAA-MM-DD.");
            }
        }
    }

    public LocalTime leerHora(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalTime.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Use HH:MM.");
            }
        }
    }
}
