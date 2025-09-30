package ar.utn.ba.ddsi.fuenteEstatica.EstrategiasExtraccion;

import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import static utils.NormalizadorTexto.normalizarTexto;

public class EstrategiaExtraccionHechoCSV implements EstrategiaExtraccionHecho {

    @Override
    public List<Hecho> obtenerHechosDesde(String pathArchivo){
        List<Hecho> hechos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(pathArchivo))) {
            String[] encabezado = reader.readNext();
            Map<String, Integer> indices = this.obtenerIndicesColumnas(encabezado);
            this.validarColumnasObligatorias(indices);

            String[] fila;
            while ((fila = reader.readNext()) != null) {
                hechos.add(this.instanciarHechoDesdeFila(fila, indices));
            }
        }
        catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            //TODO
        }

        return hechos;
    }

    private Map<String, Integer> obtenerIndicesColumnas(String[] encabezado) {
        Map<String, Integer> indices = new HashMap<>();
        Set<String> columnasValidas = Set.of(
                "titulo", "descripcion", "categoria", "latitud", "longitud", "fecha del hecho"
        );

        for (int i = 0; i < encabezado.length; i++) {
            String columna = normalizarTexto(encabezado[i])
                    .replace("\uFEFF", "")   // elimina BOM si está
                    .toLowerCase()
                    .trim();

            if (columnasValidas.contains(columna)) {
                indices.put(columna, i);
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

        LocalDateTime fechaHecho = LocalDate.parse(fecha, FORMATO_FECHA).atStartOfDay();
        Ubicacion nuevaUbi = new Ubicacion();
        nuevaUbi.setLatitud(latitud);
        nuevaUbi.setLongitud(longitud);

        Categoria cat =  new Categoria(categoria);
        return new Hecho(titulo,descripcion,cat,nuevaUbi,fechaHecho,null,Origen.DATASET,FuenteOrigen.ESTATICO,LocalDateTime.now(),true);
    }
}
