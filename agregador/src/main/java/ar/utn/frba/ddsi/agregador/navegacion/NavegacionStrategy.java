package ar.utn.frba.ddsi.agregador.navegacion;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import java.util.List;

public interface NavegacionStrategy {
    List<Hecho> navegar(List<Hecho> hechos);
}
