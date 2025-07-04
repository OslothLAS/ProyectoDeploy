package entities.colecciones.consenso.strategies;

import entities.hechos.Hecho;
import entities.colecciones.Fuente;
import org.springframework.stereotype.Component;

import java.util.*;
@Component("MULTIPLE_MENCION")
public class ConsensoMultipleMencionStrategy extends ConsensoStrategy{

    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
        return super.obtenerHechos(fuentes, hechos, 2);
    }
}
