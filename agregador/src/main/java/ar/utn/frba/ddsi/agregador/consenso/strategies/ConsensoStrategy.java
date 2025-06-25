package ar.utn.frba.ddsi.agregador.consenso.strategies;

import ar.utn.frba.ddsi.agregador.consenso.Consenso;
import entities.hechos.Hecho;
import entities.colecciones.Fuente;

import java.util.List;

public interface ConsensoStrategy {
    List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes);
}
