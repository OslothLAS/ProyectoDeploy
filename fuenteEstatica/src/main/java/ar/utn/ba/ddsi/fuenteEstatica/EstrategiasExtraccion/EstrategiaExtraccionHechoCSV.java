package ar.utn.ba.ddsi.fuenteEstatica.EstrategiasExtraccion;

import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static utils.NormalizadorTexto.normalizarTexto;

@Slf4j
@Component
public class EstrategiaExtraccionHechoCSV implements EstrategiaExtraccionHecho {


    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String getExtensionSoportada() {
        return "csv";
    }

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

    @Override
    public List<Hecho> obtenerHechosDesde(InputStream inputStream){
        List<Hecho> hechos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String[] encabezado = reader.readNext();
            Map<String, Integer> indices = this.obtenerIndicesColumnas(encabezado);
            this.validarColumnasObligatorias(indices);

            String[] fila;
            int numLinea = 1;

            while ((fila = reader.readNext()) != null) {
                numLinea++;
                try {
                    hechos.add(this.instanciarHechoDesdeFila(fila, indices));
                } catch (Exception e) {
                    log.warn("Error al procesar la línea {} del CSV: {}. Fila omitida.", numLinea, e.getMessage());
                }
            }
        }
        catch (IOException | CsvValidationException e) {
            log.error("Error crítico al leer el archivo CSV. El proceso se ha detenido.", e);
            throw new RuntimeException("Error al leer el CSV", e);
        }

        log.info("Se procesaron {} hechos desde el CSV.", hechos.size());
        return hechos;
    }

    private Map<String, Integer> obtenerIndicesColumnas(String[] encabezado) {
        Map<String, Integer> indices = new HashMap<>();
        Set<String> columnasValidas = Set.of(
                "titulo", "descripcion", "categoria", "latitud", "longitud", "fecha del hecho"
        );

        for (int i = 0; i < encabezado.length; i++) {
            String columna = normalizarTexto(encabezado[i])
                    .replace("\uFEFF", "")
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
                throw new IllegalArgumentException("Columna obligatoria faltante en el CSV: " + columna);
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

        String latLimpia = latitud;
        String lonLimpia = longitud;

        LocalDateTime fechaHecho = LocalDate.parse(fecha, FORMATO_FECHA).atStartOfDay();

        Ubicacion nuevaUbi = new Ubicacion();

        nuevaUbi.setLatitud(latLimpia);
        nuevaUbi.setLongitud(lonLimpia);

        Categoria cat = new Categoria(categoria);

        return new Hecho(titulo,descripcion,cat,nuevaUbi,fechaHecho,null,Origen.DATASET,FuenteOrigen.ESTATICO,LocalDateTime.now(),true);
    }
}