package entities.fuentes;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import entities.hechos.Hecho;
import entities.hechos.Origen;
import entities.hechos.Ubicacion;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.Normalizer;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

//necesito dar soporte para todos los tipos de archivos posibles
public class FuenteEstatica {
    private String[] pathArchivos;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    //ir agregando mas cases a medida que van viniendo mas tipos de archivos...
    public Map<String, Hecho> obtenerHechos() {
        Map<String, Hecho> hechosPorTitulo = new LinkedHashMap<>();

        for (String pathArchivo : this.pathArchivos) {
            try {
                String extension = obtenerExtensionArchivo(pathArchivo);

                switch (extension.toLowerCase()) {
                    case "csv":
                        hechosPorTitulo.putAll(obtenerHechosCSV(pathArchivo));
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

    private String normalizarTexto(String texto) {
        // SACA TILDES Y MAYUSCULAS
        String textoSinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoSinTildes.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

    private Map<String, Hecho> obtenerHechosCSV(String pathArchivo) throws IOException, CsvValidationException {
        Map<String, Hecho> hechos = new LinkedHashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(pathArchivo))) {
            String[] encabezado = reader.readNext();
            Map<String, Integer> indices = this.obtenerIndicesColumnas(encabezado);
            this.validarColumnasObligatorias(indices);

            String[] fila;
            while ((fila = reader.readNext()) != null) {
                String titulo = fila[indices.get("titulo")].trim();
                hechos.put(titulo, this.instanciarHechoDesdeFila(fila, indices));
            }
        }

        return hechos;
    }

    private Map<String, Integer> obtenerIndicesColumnas(String[] encabezado) {
        Map<String, Integer> indices = new HashMap<>();
        for (int i = 0; i < encabezado.length; i++) {
            String columna = this.normalizarTexto(encabezado[i].trim());
            switch (columna) {
                case "titulo":
                case "descripcion":
                case "categoria":
                case "latitud":
                case "longitud":
                case "fecha del hecho":
                    indices.put(columna, i);
                    break;
            }
        }
        return indices;
    }

    private void validarColumnasObligatorias(Map<String, Integer> indices) {
        String[] columnasObligatorias = {"titulo", "descripcion", "categoria", "latitud", "longitud", "fecha del hecho"};
        for (String columna : columnasObligatorias) {
            if (!indices.containsKey(columna)) {
                throw new RuntimeException("Faltan columnas obligatorias en el CSV: " +
                        String.join(", ", columnasObligatorias));
            }
        }
    }

    private Hecho instanciarHechoDesdeFila(String[] fila, Map<String,Integer> indices){
        String titulo = fila[indices.get("titulo")].trim();
        String descripcion = fila[indices.get("descripcion")].trim();
        String categoria = fila[indices.get("categoria")].trim();
        String latitud = fila[indices.get("latitud")].trim();
        String longitud = fila[indices.get("longitud")].trim();
        String fecha = fila[indices.get("fecha del hecho")].trim();

        LocalDate fechaHecho = LocalDate.parse(fecha, FORMATO_FECHA);
        Ubicacion nuevaUbi = new Ubicacion(latitud, longitud);

        return new Hecho(titulo, descripcion, categoria, nuevaUbi, fechaHecho,Origen.DATASET);
    }

    public FuenteEstatica(String[] pathArchivos_) {
        pathArchivos = pathArchivos_;
    }
}
