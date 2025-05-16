package EstrategiasExtraccion;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.hechos.Origen;
import entities.hechos.Ubicacion;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static utils.ExtensionReader.normalizarTexto;

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
        for (int i = 0; i < encabezado.length; i++) {
            String columna = normalizarTexto(encabezado[i].trim());
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

        DatosHechos data = new DatosHechos(titulo,descripcion, categoria, nuevaUbi, fechaHecho,LocalDate.now(), Origen.DATASET);
        return Hecho.create(data);
    }


}
