package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import entities.hechos.Hecho;


import java.util.List;

public interface IColeccionService {
    void createColeccion(ColeccionInputDTO coleccion);
    List<Hecho> getColeccion(Long idColeccion, String modoNavegacion);
    void actualizarHechos();
    void consensuarHechos();
}
