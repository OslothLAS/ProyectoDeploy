package ar.utn.ba.ddsi.fuenteDinamica.services;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.TokenInfo;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;

import java.util.List;
import java.util.Map;

public interface IHechoService {
    HechoOutputDTO crearHecho(HechoDTO hecho, TokenInfo token);
    void editarHecho(Long idHecho, HechoDTO dto, TokenInfo tokenInfo) throws Exception;
    List<Hecho> obtenerTodos(Map<String, String> filtros);
    void invalidarHechoPorTituloYDescripcion(String titulo, String descripcion);
    HechoDTO getHechoById(Long id);
    List<HechoDTO> getAllHechos();
    List<HechoDTO> getHechosByUsername(String username);
}
