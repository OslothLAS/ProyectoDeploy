package entities.colecciones.consenso.strategies;

import entities.hechos.Hecho;
import entities.colecciones.Fuente;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ABSOLUTA")
@Getter
public class ConsensoAbsolutaStrategy extends ConsensoStrategy{
    public final String nombre = "absoluta";

    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
            return super.obtenerHechos(fuentes, hechos, fuentes.size());
    }
}
