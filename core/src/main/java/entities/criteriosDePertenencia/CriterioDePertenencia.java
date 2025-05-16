package entities.criteriosDePertenencia;

import entities.hechos.Hecho;

public interface CriterioDePertenencia {
    boolean cumpleCriterio(Hecho hecho);
}
