package ar.utn.ba.ddsi.fuenteEstatica.services;

import entities.hechos.Hecho;

import java.util.List;
import java.util.Map;

public interface IExtractService {
    List<Hecho> getHechos(Map<String, String> filtros);
    void invalidarHechoPorTituloYDescripcion(String titulo, String descripcion);
}
