package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.ColeccionOutputDTO;
import entities.hechos.Hecho;


import java.util.List;

public interface IColeccionService {
    public void createColeccion(ColeccionInputDTO coleccion);
    public List<Hecho> getColeccion(String idColeccion, String modoNavegacion);
    public void actualizarHechos();

}
