package entities.colecciones.consenso;

import entities.colecciones.consenso.strategies.ConsensoStrategy;
import entities.colecciones.Fuente;
import entities.hechos.Hecho;

import java.util.List;

public class Consenso {
    private ConsensoStrategy strategy;

    public List<Hecho> obtenerHechosConConsenso(List<Fuente> fuentes, List<Hecho> hechos){
        return this.strategy.obtenerHechosConsensuados(fuentes, hechos);
    }

    public void cambiarEstrategia(ConsensoStrategy strategy){
        this.strategy = strategy;
    }
}
