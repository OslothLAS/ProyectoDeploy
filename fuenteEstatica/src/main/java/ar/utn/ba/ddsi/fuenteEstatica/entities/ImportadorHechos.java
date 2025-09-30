package ar.utn.ba.ddsi.fuenteEstatica.entities;

import ar.utn.ba.ddsi.fuenteEstatica.EstrategiasExtraccion.EstrategiaExtraccionHecho;
import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.Hecho;

import ar.utn.ba.ddsi.fuenteEstatica.EstrategiasExtraccion.EstrategiaExtraccionHechoCSV;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import utils.ConfigReader;
import utils.NormalizadorTexto;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
@Component
public class ImportadorHechos {
    private final ConfigReader config;
    private final String[] pathArchivos;
    private final Map<String, EstrategiaExtraccionHecho> estrategiasPorExtension;
    private final Set<String> clavesHechosInvalidos = ConcurrentHashMap.newKeySet();
    //por ahora persistimos en memoria...    ):

    private String claveHecho(String titulo, String descripcion) {
        return titulo.trim() + "::" + descripcion.trim();
    }

    public List<Hecho> obtenerHechos() {
        List<Hecho> hechos = new ArrayList<>();

        for (String pathArchivo : this.pathArchivos) {
            try {
                String extension = utils.ExtensionReader.getFileExtension(pathArchivo);
                List<Hecho> hechosDelArchivo = this.procesarArchivo(pathArchivo, extension);
                for (Hecho hecho : hechosDelArchivo) {
                    boolean valido = !clavesHechosInvalidos.contains(claveHecho(hecho.getTitulo(), hecho.getDescripcion()));
                    hecho.setEsValido(valido);
                }
                hechos.addAll(hechosDelArchivo);
            }catch (Exception e) {
                log.error("Error procesando archivo {}: {}", pathArchivo, e.getMessage(), e);
            }
        }
        return hechos;
    }

    private List<Hecho> procesarArchivo(String pathArchivo, String extension) {
        EstrategiaExtraccionHecho estrategia = estrategiasPorExtension.get(NormalizadorTexto.normalizarTexto(extension));

        if (estrategia == null) {
            throw new UnsupportedOperationException("Formato no soportado: " + extension);
        }

        return estrategia.obtenerHechosDesde(pathArchivo);
    }

    public void invalidarHechoPorTituloYDescripcion(String titulo, String descripcion) {
        clavesHechosInvalidos.add(claveHecho(titulo, descripcion));
    }

    public ImportadorHechos() {
        this.config = new ConfigReader(); // <-- sin pasar path
        this.pathArchivos = config.getPathsAsArray("filePaths", ",");
        this.estrategiasPorExtension = new ConcurrentHashMap<>();
        this.estrategiasPorExtension.put("csv", new EstrategiaExtraccionHechoCSV());
    }
}
