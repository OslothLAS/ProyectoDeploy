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

public class FuenteEstatica {


    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String normalizarTexto(String texto) {
        // SACA TILDES Y MAYUSCULAS
        String textoSinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoSinTildes.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

    public Map<String, Hecho> obtenerHechos(String pathCSV){
        Map<String, Hecho> hechosPorTitulo = new LinkedHashMap<>();

        try(CSVReader reader = new CSVReader(new FileReader(pathCSV))){
            String[] encabezado = reader.readNext();
            Map<String,Integer> indices = this.obtenerIndicesColumnas(encabezado);
            this.validarColumnasObligatorias(indices);


            //estas lineas son solo para el debug
            System.out.println("Encabezado detectado:");
            System.out.println(Arrays.toString(Arrays.stream(encabezado).map(this::normalizarTexto).toArray()));
            //-------------------------------------------------


            String[] fila;
            while(((fila = reader.readNext()) != null)){
                hechosPorTitulo.put(fila[indices.get("titulo")].trim(), this.instanciarHechoDesdeFila(fila,indices));
            }
        }
        catch (IOException | CsvValidationException e){
            System.out.println("Error al leer el archivo");
        }

        return hechosPorTitulo;
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
}
