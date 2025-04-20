package entities.fuentes;

import entities.hechos.Hecho;
import EstrategiasExtraccion.EstrategiaExtraccionHecho;
import EstrategiasExtraccion.EstrategiaExtraccionHechoCSV;
import lombok.Getter;
import utils.*;


import java.io.ObjectInputFilter;
import java.util.*;

//necesito dar soporte para todos los tipos de archivos posibles
@Getter
public class FuenteEstatica {
    private ConfigReader config;
    String[] pathArchivos;

    private EstrategiaExtraccionHecho estrategiaExtraccionHecho;

    //ir agregando mas cases a medida que van viniendo mas tipos de archivos...
    public Map<String, Hecho> obtenerHechos() {
        Map<String, Hecho> hechosPorTitulo = new LinkedHashMap<>();

        for (String pathArchivo : this.pathArchivos) {
            try {
                String extension = utils.ExtensionReader.getFileExtension(pathArchivo);

                switch (extension.toLowerCase()) {
                    case "csv":
                        EstrategiaExtraccionHecho estrategiaExtraccionHechoCSV = new EstrategiaExtraccionHechoCSV();
                        hechosPorTitulo.putAll(estrategiaExtraccionHechoCSV.obtenerHechosDesde(pathArchivo));
                        break;
                    default:
                        System.err.println("Formato no soportado: " + extension);
                }
            } catch (Exception e) {
                System.err.println("Error procesando archivo " + pathArchivo + ": " + e.getMessage());
            }
        }

        return hechosPorTitulo;
    }


    public FuenteEstatica(String name) {
        config = new ConfigReader(name);
        pathArchivos = config.getPathsAsArray("filePaths", ",");
    }
}
