package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.FuenteInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.ColeccionOutputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.CriterioDePertenenciaDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.HechoOutputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.consenso.strategies.TipoConsenso;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import java.util.List;
import java.util.Map;

public interface IColeccionService {
    void createColeccion(ColeccionInputDTO coleccion);
    List<ColeccionOutputDTO> getColecciones();
    List<Coleccion> getColeccionesClass();
    List<Hecho> getHechosDeColeccion(Long idColeccion, String modoNavegacion, Map<String,String> filtros);
    void actualizarHechos();
    void consensuarHechos();
    void deleteColeccion(Long idColeccion);
    void cambiarConsenso(Long idColeccion, TipoConsenso tipo);
    void agregarFuente(Long idColeccion, FuenteInputDTO fuente);
    void eliminarFuente(Long idColeccion, Long idFuente);
    List<HechoOutputDTO> obtenerTodosLosHechos(Map<String,String> filtros);
    List<StatDTO> getProvinciaMasReportadaPorTodasLasColecciones();
    List<StatDTO> getCategoriaMasReportada();
    List<StatDTO> getHoraMasReportada();
    List<StatDTO> getProviniciaMasReportadaPorCategoria();
    List<Fuente> getFuentes();
    List<CriterioDePertenenciaDTO> getCriterios();
}
