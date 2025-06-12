package entities.criteriosDePertenencia;

import entities.hechos.Hecho;
import org.springframework.util.MultiValueMap;

public interface CriterioDePertenencia {
    boolean cumpleCriterio(Hecho hecho);

    MultiValueMap<String, String> aQueryParam();
}
