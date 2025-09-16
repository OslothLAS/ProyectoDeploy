package entities.colecciones.consenso.strategies;

import entities.colecciones.Fuente;
import entities.hechos.Hecho;

import java.util.List;

public interface IAlgoritmoConsenso {
    String getNombre();
    List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos);
}
