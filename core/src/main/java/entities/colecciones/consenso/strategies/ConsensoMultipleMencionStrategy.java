package entities.colecciones.consenso.strategies;

import entities.hechos.Hecho;
import entities.colecciones.Fuente;

import java.util.*;

public class ConsensoMultipleMencionStrategy extends ConsensoStrategy{

    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
        return super.obtenerHechos(fuentes, hechos, 2);
    }
}
