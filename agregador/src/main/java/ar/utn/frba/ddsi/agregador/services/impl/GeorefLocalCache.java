package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Localidad;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Provincia;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Ubicacion;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GeorefLocalCache {

    @Value("${georef.provincias.path:provincias.json}")
    private String provinciasPath;

    @Value("${georef.localidades.path:localidades.json}")
    private String localidadesPath;

    private List<Provincia> provincias;
    private List<LocalidadConCoordenadas> localidadesConCoordenadas;
    private Map<String, Provincia> provinciasPorId;

    // Clase interna para mantener coordenadas en memoria sin modificar la entidad
    private static class LocalidadConCoordenadas {
        Localidad localidad;
        double latitud;
        double longitud;

        LocalidadConCoordenadas(Localidad localidad, double latitud, double longitud) {
            this.localidad = localidad;
            this.latitud = latitud;
            this.longitud = longitud;
        }
    }

    @PostConstruct
    public void init() {
        log.info("Inicializando GeorefLocalCache...");
        cargarProvincias();
        cargarLocalidades();
        log.info("GeorefLocalCache inicializado con {} provincias y {} localidades",
                provincias.size(), localidadesConCoordenadas.size());
    }

    private void cargarProvincias() {
        provincias = new ArrayList<>();
        provinciasPorId = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(
                    getClass().getClassLoader().getResourceAsStream(provinciasPath)
            );
            JsonNode provinciasArray = root.get("provincias");

            if (provinciasArray != null && provinciasArray.isArray()) {
                for (JsonNode provinciaNode : provinciasArray) {
                    String id = provinciaNode.get("id").asText();
                    String nombre = provinciaNode.get("nombre").asText();

                    Provincia provincia = new Provincia(nombre);
                    provincias.add(provincia);
                    provinciasPorId.put(id, provincia);
                }
            }
        } catch (IOException e) {
            log.error("Error al cargar provincias desde {}", provinciasPath, e);
            throw new RuntimeException("No se pudo cargar el archivo de provincias", e);
        }
    }

    private void cargarLocalidades() {
        localidadesConCoordenadas = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(
                    getClass().getClassLoader().getResourceAsStream(localidadesPath)
            );
            JsonNode localidadesArray = root.get("localidades");

            if (localidadesArray != null && localidadesArray.isArray()) {
                for (JsonNode localidadNode : localidadesArray) {
                    String nombre = localidadNode.get("nombre").asText();
                    String provinciaId = localidadNode.get("provincia").get("id").asText();

                    JsonNode centroide = localidadNode.get("centroide");
                    double lat = centroide.get("lat").asDouble();
                    double lon = centroide.get("lon").asDouble();

                    Provincia provincia = provinciasPorId.get(provinciaId);
                    if (provincia != null) {
                        Localidad localidad = new Localidad(provincia, nombre);
                        LocalidadConCoordenadas lcc = new LocalidadConCoordenadas(localidad, lat, lon);
                        localidadesConCoordenadas.add(lcc);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error al cargar localidades desde {}", localidadesPath, e);
            throw new RuntimeException("No se pudo cargar el archivo de localidades", e);
        }
    }

    /**
     * Busca la localidad más cercana a las coordenadas dadas.
     * Utiliza la fórmula de Haversine para calcular distancias.
     *
     * @param latitud Latitud en grados decimales
     * @param longitud Longitud en grados decimales
     * @return Ubicacion con la localidad más cercana encontrada
     */
    public Ubicacion buscar(String latitud, String longitud) {
        if (localidadesConCoordenadas == null || localidadesConCoordenadas.isEmpty()) {
            throw new IllegalStateException("El cache de localidades no está inicializado");
        }

        double lat = Double.parseDouble(latitud);
        double lon = Double.parseDouble(longitud);

        LocalidadConCoordenadas localidadCercana = null;
        double distanciaMinima = Double.MAX_VALUE;

        for (LocalidadConCoordenadas lcc : localidadesConCoordenadas) {
            double distancia = calcularDistanciaHaversine(
                    lat, lon,
                    lcc.latitud, lcc.longitud
            );

            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                localidadCercana = lcc;
            }
        }

        if (localidadCercana == null) {
            throw new RuntimeException("No se encontró ninguna localidad cercana");
        }

        return new Ubicacion(
                latitud,
                longitud,
                localidadCercana.localidad
        );
    }

    /**
     * Calcula la distancia entre dos puntos geográficos usando la fórmula de Haversine.
     *
     * @return Distancia en kilómetros
     */
    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int RADIO_TIERRA_KM = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA_KM * c;
    }

    public List<Localidad> getLocalidades() {
        return localidadesConCoordenadas.stream()
                .map(lcc -> lcc.localidad)
                .toList();
    }
}