package EstrategiasExtraccion;

import com.opencsv.exceptions.CsvValidationException;
import entities.hechos.Hecho;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public interface EstrategiaExtraccionHecho {
    DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    Map<String, Hecho> obtenerHechosDesde(String pathArchivo);
}
