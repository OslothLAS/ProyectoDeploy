package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NormalizadorHecho {

    private static final Map<String, String> CATEGORIA = Map.of(
            "alud", "derrumbe",
            "Fuego arrasador en zona boscosa","incendio forestal",
            "Fuego arrasador en bosque nativo","incendio forestal",
            "crisis sanitaria", "evento sanitario",
            "incidente tecnológico", "desastre tecnológico"
    );

    private static final Map<String, String> PROVINCIA = Map.of(
            "BsAs", "Buenos Aires",
            "CABA", "Ciudad Autonoma de Buenos Aires"
    );

    private static final Map<String, String> CIUDAD = Map.of(
            "BsAs", "Buenos Aires",
            "CABA", "Ciudad Autonoma de Buenos Aires"
    );

    public static String normalize(String input, Map<String, String> textoANormalizar) {
        if (input == null) return null;

        String lowerInput = input.toLowerCase();
        String normalized = input;

        for (Map.Entry<String, String> rule : textoANormalizar.entrySet()) {
            if (lowerInput.contains(rule.getKey())) {
                normalized = rule.getValue();
                break;
            }
        }

        return normalized;
    }

    public static String convertToDDMMYYYY(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        String trimmedDate = dateString.trim();


        try {
            DateTimeFormatter usFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            LocalDate date = LocalDate.parse(trimmedDate, usFormatter);

            DateTimeFormatter euFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return date.format(euFormatter);

        } catch (DateTimeParseException e1) {
            try {
                DateTimeFormatter euFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                LocalDate date = LocalDate.parse(trimmedDate, euFormatter);

                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return date.format(outputFormatter);

            } catch (DateTimeParseException e2) {
                return trimmedDate; // Devolver original si ningún formato funciona
            }
        }
    }


    public static String normalizarCategoria(String categoria) {
      return  normalize(categoria, CATEGORIA);
    }

    public static String normalizarUbicacion(String ubicacion) {
        return  normalize(ubicacion, UBICACION);
    }
}