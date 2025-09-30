package ar.utn.frba.ddsi.agregador.models.entities.colecciones.consenso.strategies;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;

import java.util.List;

public interface IAlgoritmoConsenso {
    String getNombre();
    List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos);
}
