package ar.utn.frba.ddsi.agregador.models.repositories;



import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.hechos.Hecho;

import java.util.List;

public interface IHechoRepository {
    void save(Hecho hecho);
    Hecho findById(Long id);
    List<Hecho> findAll();
    List<Hecho> findSegunCriterios(List<CriterioDePertenencia> criterios);
}
