package ar.utn.ba.ddsi.fuenteEstatica.EstrategiasExtraccion;

import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.Hecho;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface EstrategiaExtraccionHecho {
    DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    List<Hecho> obtenerHechosDesde(String pathArchivo);
}
