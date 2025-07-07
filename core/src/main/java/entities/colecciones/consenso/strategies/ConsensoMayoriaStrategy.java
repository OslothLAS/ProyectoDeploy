package entities.colecciones.consenso.strategies;

import entities.hechos.Hecho;
import entities.colecciones.Fuente;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("MAYORIA")
@Getter
public class ConsensoMayoriaStrategy extends ConsensoStrategy{
    public final String nombre = "mayoria";

    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
        int cantidadDeFuentes = (fuentes.size() / 2) + 1;
        return super.obtenerHechos(fuentes, hechos, cantidadDeFuentes);
    }

}
