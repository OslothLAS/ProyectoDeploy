package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.FuenteInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import entities.colecciones.Coleccion;
import entities.colecciones.consenso.strategies.TipoConsenso;
import entities.hechos.Hecho;


import java.util.List;

public interface IColeccionService {
    void createColeccion(ColeccionInputDTO coleccion);
    List<Coleccion> getColecciones();
    List<Hecho> getHechosDeColeccion(Long idColeccion, String modoNavegacion);
    void actualizarHechos();
    void consensuarHechos();
    void deleteColeccion(Long idColeccion);
    void cambiarConsenso(Long idColeccion, TipoConsenso tipo);
    void agregarFuente(Long idColeccion, FuenteInputDTO fuente);
    void eliminarFuente(Long idColeccion, Long idFuente);
    List<StatDTO> getProvinciaMasReportada(Long idColeccion);
    List<StatDTO> getCategoriaMasReportada();
    List<StatDTO> getHoraMasReportada(Long idCategoria);
    List<StatDTO> getProviniciaMasReportadaPorCategoria(Long idCategoria);
}
