package ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;

public interface CriterioDePertenencia {
    boolean cumpleCriterio(Hecho hecho);
}
