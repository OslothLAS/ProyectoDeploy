package ar.utn.ba.ddsi.fuenteDinamica.services;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;

import java.util.List;
import java.util.Map;

public interface IHechoService {
    void crearHecho(HechoDTO hecho);
    void editarHecho(Long idHecho, HechoDTO dto) throws Exception;
    List<Hecho> obtenerTodos(Map<String, String> filtros);
    void invalidarHechoPorTituloYDescripcion(String titulo, String descripcion);
}
