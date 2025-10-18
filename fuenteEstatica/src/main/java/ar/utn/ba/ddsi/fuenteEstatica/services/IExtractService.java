package ar.utn.ba.ddsi.fuenteEstatica.services;

import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.Hecho;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IExtractService {
    List<Hecho> getHechos(Map<String, String> filtros);
    void invalidarHechoPorTituloYDescripcion(String titulo, String descripcion);
    List<Hecho> importarHechosDesdeArchivo(InputStream inputStream, String fileName);
    List<Hecho> getHechosMemoria(Map<String, String> filtros);
}
