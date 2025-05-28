package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;

import java.util.List;

public interface IHechoRepository {
    void save(Hecho hecho);
    Hecho findById(Long id);
    List<Hecho> findAll();
    List<Hecho> findSegunCriterios(List<CriterioDePertenencia> criterios);
}
