package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigLoader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("spam-config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("No se encontró el archivo de configuración spam-config.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar configuración de spam", e);
        }
    }

    public static double getSpamThreshold() {
        return Double.parseDouble(properties.getProperty("spam.threshold", "5.0"));
    }

    public static Map<String, Double> getSpamTermsIdf() {
        String terms = properties.getProperty("spam.terms", "");
        return Arrays.stream(terms.split(","))
                .map(term -> term.split(":"))
                .filter(pair -> pair.length == 2)
                .collect(Collectors.toMap(
                        pair -> pair[0],
                        pair -> Double.parseDouble(pair[1])
                ));
    }
}