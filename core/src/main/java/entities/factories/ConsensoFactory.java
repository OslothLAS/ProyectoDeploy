package entities.factories;

import entities.colecciones.consenso.strategies.Absoluta;
import entities.colecciones.consenso.strategies.Mayoria;
import entities.colecciones.consenso.strategies.MultipleMencion;
import entities.colecciones.consenso.strategies.IAlgoritmoConsenso;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

public class ConsensoFactory {
    private static Map<String, IAlgoritmoConsenso> strategies;

    public ConsensoFactory(@Qualifier("ABSOLUTA") IAlgoritmoConsenso absoluta,
                           @Qualifier("MAYORIA") IAlgoritmoConsenso mayoria,
                           @Qualifier("MULTIPLE_MENCION") IAlgoritmoConsenso multipleMencion) {
        strategies = Map.of(
                "ABSOLUTA", absoluta,
                "MAYORIA", mayoria,
                "MULTIPLE_MENCION", multipleMencion
        );
    }

    static {
        strategies = Map.of(
                "ABSOLUTA", new Absoluta(),
                "MAYORIA", new Mayoria(),
                "MULTIPLE_MENCION", new MultipleMencion()
        );
    }

    public static IAlgoritmoConsenso getStrategy(String modoNavegacion) {
        return strategies.getOrDefault(modoNavegacion.toUpperCase(), null);
    }
}
