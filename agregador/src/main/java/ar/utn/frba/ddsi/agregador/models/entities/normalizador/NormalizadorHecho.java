package ar.utn.frba.ddsi.agregador.models.entities.normalizador;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    private static String convertToDDMMYYYY(LocalDate date) {
            if (date == null) {
                return null;
            }

            DateTimeFormatter formatoDiaMesAnio = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return date.format(formatoDiaMesAnio);
        }




    public static String normalizarCategoria(String categoria) {
      return  normalize(categoria, CATEGORIA);
    }

    public static List<String> normalizarUbicacion(double latitud, double longitud) {
        return OpenStreetMap.obtenerUbicacion(latitud, longitud);
    }


    //Hice este por si en algun momento tenemos que convertir el localdate en string
    public static String normalizarFecha(LocalDate localDate) {
        return convertToDDMMYYYY(localDate);
    }
}