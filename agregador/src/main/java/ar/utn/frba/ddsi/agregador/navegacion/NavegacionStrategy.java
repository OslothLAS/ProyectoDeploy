package ar.utn.frba.ddsi.agregador.navegacion;

import entities.colecciones.Coleccion;
import entities.hechos.Hecho;

import java.util.List;

public interface NavegacionStrategy {
    List<Hecho> navegar(Coleccion coleccion, List<Hecho> hechos);
}
