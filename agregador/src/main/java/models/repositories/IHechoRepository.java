package models.repositories;

import models.entities.hechos.Hecho;

import java.util.List;
import java.util.Optional;

public interface IHechoRepository {
    void save(Hecho hecho);
    Optional<Hecho> findById(Long id);
    List<Hecho> findAll();
}
