package ar.utn.ba.ddsi.fuenteEstatica.EstrategiasExtraccion;

import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.Hecho;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public interface EstrategiaExtraccionHecho {
    DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    List<Hecho> obtenerHechosDesde(String pathArchivo);

    List<Hecho> obtenerHechosDesde(InputStream inputStream);

    String getExtensionSoportada();
}