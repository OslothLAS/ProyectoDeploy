package fileTypes;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import entities.hechos.Hecho;
import entities.hechos.Origen;
import entities.hechos.Ubicacion;

import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileTypeCSV implements FileType{

    public Map<String, Hecho> obtenerHechosDesde(String pathArchivo) throws IOException, CsvValidationException {
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

        return new Hecho(titulo, descripcion, categoria, nuevaUbi, fechaHecho, Origen.DATASET);
    }

    private String normalizarTexto(String texto) {
        // SACA TILDES Y MAYUSCULAS
        String textoSinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoSinTildes.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }
}
