package ar.utn.frba.ddsi.agregador.models.repositories;


import entities.colecciones.Coleccion;

import java.util.List;

public interface IColeccionRepository {
    void save(Coleccion coleccion);
    Coleccion findById(String handleValue);
    List<Coleccion> findAll();
}
