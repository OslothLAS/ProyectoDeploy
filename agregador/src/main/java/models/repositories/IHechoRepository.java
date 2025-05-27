package models.repositories;

import models.entities.criteriosDePertenencia.CriterioDePertenencia;
import models.entities.hechos.Hecho;

import java.util.List;
import java.util.Optional;

public interface IHechoRepository {
    void save(Hecho hecho);
    Hecho findById(Long id);
    List<Hecho> findAll();
    List<Hecho> findSegunCriterios(List<CriterioDePertenencia> criterios);
}
