package entities.fuentes;

import entities.hechos.Hecho;
import fileTypes.FileType;
import fileTypes.FileTypeCSV;


import java.util.*;

//necesito dar soporte para todos los tipos de archivos posibles
public class FuenteEstatica {
    private String[] pathArchivos;
    
    //ir agregando mas cases a medida que van viniendo mas tipos de archivos...
    public Map<String, Hecho> obtenerHechos() {
        Map<String, Hecho> hechosPorTitulo = new LinkedHashMap<>();

        for (String pathArchivo : this.pathArchivos) {
            try {
                String extension = obtenerExtensionArchivo(pathArchivo);

                switch (extension.toLowerCase()) {
                    case "csv":
                        FileType fileTypeCSV = new FileTypeCSV();
                        hechosPorTitulo.putAll(fileTypeCSV.obtenerHechosDesde(pathArchivo));
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

    private String obtenerExtensionArchivo(String nombreArchivo) {
        int lastDot = nombreArchivo.lastIndexOf('.');
        if (lastDot == -1) {
            return ""; // Sin extensión
        }
        return nombreArchivo.substring(lastDot + 1);
    }

    public FuenteEstatica(String[] pathArchivos_) {
        pathArchivos = pathArchivos_;
    }
}
