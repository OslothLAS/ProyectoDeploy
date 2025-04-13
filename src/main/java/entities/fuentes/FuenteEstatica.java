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
            int idxTitulo = -1;
            int idxDescripcion = -1;
            int idxCategoria = -1;
            int idxLatitud = -1;
            int idxLongitud = -1;
            int idxFecha = -1;

            for(int i = 0; i < encabezado.length; i++){
                String columna = this.normalizarTexto(encabezado[i].trim());

                switch (columna) {
                    case "titulo" -> idxTitulo = i;
                    case "descripcion" -> idxDescripcion = i;
                    case "categoria" -> idxCategoria = i;
                    case "latitud" -> idxLatitud = i;
                    case "longitud" -> idxLongitud = i;
                    case "fecha del hecho" -> idxFecha = i;
                }
            }
            if (idxTitulo == -1 || idxDescripcion == -1 || idxCategoria == -1 ||
                    idxLatitud == -1 || idxLongitud == -1 || idxFecha == -1) {
                throw new RuntimeException("Faltan columnas obligatorias en el CSV: 'titulo', 'descripcion', 'categoria', 'latitud', 'longitud', 'fechadelhecho'");
            }

            //estas lineas son solo para el debug
            System.out.println("Encabezado detectado:");
            System.out.println(Arrays.toString(Arrays.stream(encabezado).map(this::normalizarTexto).toArray()));
            //-------------------------------------------------

            String[] fila;
            while(((fila = reader.readNext()) != null)){
                String titulo = fila[idxTitulo].trim();
                String descripcion = fila[idxDescripcion].trim();
                String categoria = fila[idxCategoria].trim();
                String latitud = fila[idxLatitud].trim();
                String longitud = fila[idxLongitud].trim();
                String fecha = fila[idxFecha].trim();

                LocalDate fechaHecho = LocalDate.parse(fecha, FORMATO_FECHA);
                Ubicacion nuevaUbi = new Ubicacion(latitud,longitud);

                Hecho nuevoHecho = new Hecho(titulo,descripcion,categoria,nuevaUbi,fechaHecho, Origen.DATASET,Optional.empty());
                hechosPorTitulo.put(titulo, nuevoHecho);
            }
        }
        catch (IOException | CsvValidationException e){
            System.out.println("Error al leer el archivo");
        }

        return hechosPorTitulo;
    }

}
