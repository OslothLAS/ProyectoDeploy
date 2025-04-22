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
                        this.createCSVextractor(hechosPorTitulo,pathArchivo);
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


    public void createCSVextractor(Map<String, Hecho> hechos,String archivo){
        EstrategiaExtraccionHechoCSV estrategiaExtraccionHechoCSV = new EstrategiaExtraccionHechoCSV();
        hechos.putAll(estrategiaExtraccionHechoCSV.obtenerHechosDesde(archivo));
    }

    public FuenteEstatica() {
        this.config = new ConfigReader(); // <-- sin pasar path
        this.pathArchivos = config.getPathsAsArray("filePaths", ",");
    }
}
