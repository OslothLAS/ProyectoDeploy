package entities.colecciones.consenso.strategies;

import entities.hechos.Hecho;
import entities.colecciones.Fuente;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("MAYORIA")
@Getter
public class Mayoria implements IAlgoritmoConsenso {
    public final String nombre = "mayoria";

    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
        int cantidadDeFuentes = (fuentes.size() / 2) + 1;
        return FiltroConsensuados.obtenerHechos(fuentes, hechos, cantidadDeFuentes);
    }

}
