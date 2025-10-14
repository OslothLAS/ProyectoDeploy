package ar.utn.frba.ddsi.agregador.navegacion;


import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;

import java.util.List;

public interface NavegacionStrategy {
    List<Hecho> navegar(Coleccion coleccion, List<Hecho> hechos);
}
