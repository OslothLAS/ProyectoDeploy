package ar.utn.frba.ddsi.agregador.models.repositories;

import entities.hechos.Hecho;
import java.util.List;

public interface IHechoRepository {
    void save(Hecho hecho);
    Hecho findById(Long id);
    List<Hecho> findAll();
}
