package ar.utn.frba.ddsi.agregador.models.repositories;

import entities.colecciones.Coleccion;

import java.util.List;
import java.util.Optional;


public interface IColeccionRepository {
    void save(Coleccion coleccion);
    Optional<Coleccion> findById(Long id);
    List<Coleccion> findAll();
}
