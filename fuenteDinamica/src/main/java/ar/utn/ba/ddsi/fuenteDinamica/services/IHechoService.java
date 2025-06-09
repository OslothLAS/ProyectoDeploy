package ar.utn.ba.ddsi.fuenteDinamica.services;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoInputDTO;
import entities.hechos.Hecho;

import java.util.List;
import java.util.Map;

public interface IHechoService {
    void crearHecho(HechoInputDTO hecho);
    void editarHecho(Long idHecho, HechoInputDTO dto) throws Exception;
    List<Hecho> obtenerTodos(Map<String, String> filtros);
}
