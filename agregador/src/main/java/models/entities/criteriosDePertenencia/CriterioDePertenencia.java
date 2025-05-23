package models.entities.criteriosDePertenencia;

import models.entities.hechos.Hecho;

public interface CriterioDePertenencia {
    boolean cumpleCriterio(Hecho hecho);
}
