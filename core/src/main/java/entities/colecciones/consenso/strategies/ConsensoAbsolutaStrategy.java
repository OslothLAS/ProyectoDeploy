package entities.colecciones.consenso.strategies;

import entities.hechos.Hecho;
import entities.colecciones.Fuente;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ABSOLUTA")
public class ConsensoAbsolutaStrategy extends ConsensoStrategy{

    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
            return super.obtenerHechos(fuentes, hechos, fuentes.size());
    }
}
