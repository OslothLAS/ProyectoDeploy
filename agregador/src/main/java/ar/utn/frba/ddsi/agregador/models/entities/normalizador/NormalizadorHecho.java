package ar.utn.frba.ddsi.agregador.models.entities.normalizador;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    public static List<Double> normalizarUbicaciones(String latitud, String longitud) {

        if (latitud != null && !latitud.isBlank()) {
            try {
                // Reemplaza la coma por un punto para un parseo seguro
                String latitudLimpia = latitud.replace(',', '.');
                double valor = Double.parseDouble(latitudLimpia);

                // Usa Locale.US para forzar el punto como separador decimal en el formateo
                latitud = String.format(Locale.US, "%.4f", valor);
            } catch (NumberFormatException e) {
                // Si el string no es un número válido, lo guardamos tal cual
                latitud = latitud;
            }
        } else {
            latitud = latitud;
        }


        if (longitud != null && !longitud.isBlank()) {
            try {
                String longitudLimpia = longitud.replace(',', '.');
                double valor = Double.parseDouble(longitudLimpia);
                longitud = String.format(Locale.US, "%.4f", valor);
            } catch (NumberFormatException e) {
                longitud = longitud;
            }
        } else {
            longitud = longitud;
        }

        List<Double> valoresNormalizados = new ArrayList<>();
        valoresNormalizados.add(Double.parseDouble(latitud));
        valoresNormalizados.add(Double.parseDouble(longitud));

        return valoresNormalizados;
    }


    //Hice este por si en algun momento tenemos que convertir el localdate en string
    public static String normalizarFecha(LocalDate localDate) {
        return convertToDDMMYYYY(localDate);
    }
}