package ar.utn.ba.ddsi.fuenteDinamica.services;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;

import java.util.List;

public interface IHechoService {
    void crearHecho(HechoDTO hecho);
    void editarHecho(Long idHecho, HechoDTO dto) throws Exception;
    List<Hecho> obtenerTodos();
}
