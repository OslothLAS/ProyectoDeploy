package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import entities.hechos.Hecho;


import java.util.List;

public interface IColeccionService {
    public List<Hecho> createColeccion(ColeccionInputDTO coleccion);
    public List<Hecho> getColeccion(String idColeccion);
    public void actualizarHechos();

}
