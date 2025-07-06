package ar.utn.ba.ddsi.fuenteEstatica.entities;

import ar.utn.ba.ddsi.fuenteEstatica.EstrategiasExtraccion.EstrategiaExtraccionHecho;
import entities.Importador;
import entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteEstatica.EstrategiasExtraccion.EstrategiaExtraccionHechoCSV;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import utils.ConfigReader;
import utils.NormalizadorTexto;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//necesito dar soporte para todos los tipos de archivos posibles
@Slf4j
@Getter
@Component
public class ImportadorHechos implements Importador {
    private final ConfigReader config;
    private final String[] pathArchivos;
    private final Map<String, EstrategiaExtraccionHecho> estrategiasPorExtension;


    //ir agregando mas cases a medida que van viniendo mas tipos de archivos...
    public List<Hecho> obtenerHechos() {
        List<Hecho> hechos = new ArrayList<>();

        for (String pathArchivo : this.pathArchivos) {
            try {
                String extension = utils.ExtensionReader.getFileExtension(pathArchivo);
                List<Hecho> hechosDelArchivo = this.procesarArchivo(pathArchivo, extension);
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

    public ImportadorHechos() {
        this.config = new ConfigReader(); // <-- sin pasar path
        this.pathArchivos = config.getPathsAsArray("filePaths", ",");
        this.estrategiasPorExtension = new ConcurrentHashMap<>();
        this.estrategiasPorExtension.put("csv", new EstrategiaExtraccionHechoCSV());
    }
}
