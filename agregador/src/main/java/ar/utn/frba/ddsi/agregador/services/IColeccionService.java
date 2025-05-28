package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;

import java.util.List;

public interface IColeccionService {
    public List<Hecho> createColeccion(Coleccion coleccion);
    public List<Hecho> getColeccion(String idColeccion);
    public void actualizarHechos();

}
