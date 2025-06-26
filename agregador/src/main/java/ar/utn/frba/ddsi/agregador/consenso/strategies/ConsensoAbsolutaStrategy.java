package ar.utn.frba.ddsi.agregador.consenso.strategies;

import entities.hechos.Hecho;
import entities.colecciones.Fuente;

import java.util.List;

public class ConsensoAbsolutaStrategy implements ConsensoStrategy{
    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
        return List.of();
    }
}
