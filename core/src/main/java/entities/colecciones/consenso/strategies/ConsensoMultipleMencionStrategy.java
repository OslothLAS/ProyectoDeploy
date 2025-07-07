package entities.colecciones.consenso.strategies;

import entities.hechos.Hecho;
import entities.colecciones.Fuente;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;
@Component("MULTIPLE_MENCION")
@Getter
public class ConsensoMultipleMencionStrategy extends ConsensoStrategy{
    public final String nombre = "multiple_mencion";

    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
        return super.obtenerHechos(fuentes, hechos, 2);
    }
}
