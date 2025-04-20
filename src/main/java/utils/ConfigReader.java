package utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigReader {

    private final String propertiesFilePath;

    public ConfigReader(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }


    public String[] getPathsAsArray(String propertyKey, String delimiter) {
        List<String> pathsList = getPathsAsList(propertyKey, delimiter);
        return pathsList.toArray(new String[0]);
    }


    public List<String> getPathsAsList(String propertyKey, String delimiter) {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(propertiesFilePath)) {
            prop.load(input);

            if (delimiter != null) {
                // Opción 1: Lista en una sola línea separada por delimitador (ej: "ruta1,ruta2")
                String value = prop.getProperty(propertyKey);
                if (value == null || value.trim().isEmpty()) {
                    return Collections.emptyList();
                }
                return Arrays.stream(value.split(delimiter))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo .properties: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}