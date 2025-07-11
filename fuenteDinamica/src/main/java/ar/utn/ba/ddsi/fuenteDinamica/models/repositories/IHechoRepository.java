package ar.utn.ba.ddsi.fuenteDinamica.models.repositories;

import entities.hechos.Hecho;

import java.util.List;
import java.util.Optional;

public interface IHechoRepository {
    void save(Hecho hecho);
    Optional<Hecho> findById(Long id);
    Optional<Hecho> findByTituloyDescripcion(String titulo, String descripcion);
    List<Hecho> findAll();
}
