package ar.utn.frba.ddsi.agregador.models.entities.colecciones.consenso.strategies;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ABSOLUTA")
@Getter
public class Absoluta implements IAlgoritmoConsenso {
    public final String nombre = "absoluta";

    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
            return FiltroConsensuados.obtenerHechos(fuentes, hechos, fuentes.size());
    }
}
