package entities;

import entities.hechos.Hecho;
import EstrategiasExtraccion.EstrategiaExtraccionHechoCSV;
import lombok.Getter;
import org.springframework.stereotype.Component;
import utils.ConfigReader;

import java.util.*;

//necesito dar soporte para todos los tipos de archivos posibles
@Getter
@Component
public class ImportadorHechos implements Importador {
    private final ConfigReader config;
    String[] pathArchivos;


    //ir agregando mas cases a medida que van viniendo mas tipos de archivos...
    public List<Hecho> obtenerHechos() {
        List<Hecho> hechosPorTitulo = new ArrayList<>();

        for (String pathArchivo : this.pathArchivos) {
            try {
                String extension = utils.ExtensionReader.getFileExtension(pathArchivo);

                switch (extension.toLowerCase()) {
                    case "csv":
                        hechosPorTitulo.addAll(csvExtractor(pathArchivo));
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


    public List<Hecho> csvExtractor(String archivo){
        EstrategiaExtraccionHechoCSV estrategiaExtraccionHechoCSV = new EstrategiaExtraccionHechoCSV();
        return estrategiaExtraccionHechoCSV.obtenerHechosDesde(archivo);
    }

    public ImportadorHechos() {
        this.config = new ConfigReader(); // <-- sin pasar path
        this.pathArchivos = config.getPathsAsArray("filePaths", ",");
    }
}
