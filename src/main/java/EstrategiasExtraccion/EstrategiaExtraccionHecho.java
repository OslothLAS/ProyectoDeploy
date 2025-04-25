package EstrategiasExtraccion;

import com.opencsv.exceptions.CsvValidationException;
import entities.hechos.Hecho;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public interface EstrategiaExtraccionHecho {
    DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    List<Hecho> obtenerHechosDesde(String pathArchivo);
}
