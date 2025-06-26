package ar.utn.frba.ddsi.agregador.consenso.strategies;

import entities.hechos.Hecho;
import entities.colecciones.Fuente;

import java.util.List;

public class ConsensoMayoriaStrategy extends ConsensoStrategy{

    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
        int cantidadDeFuentes = fuentes.size()/2;
        return super.obtenerHechos(fuentes, hechos, cantidadDeFuentes);
    }

}
