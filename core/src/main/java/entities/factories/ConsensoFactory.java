package entities.factories;

import entities.colecciones.consenso.strategies.ConsensoAbsolutaStrategy;
import entities.colecciones.consenso.strategies.ConsensoMayoriaStrategy;
import entities.colecciones.consenso.strategies.ConsensoMultipleMencionStrategy;
import entities.colecciones.consenso.strategies.ConsensoStrategy;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

public class ConsensoFactory {
    private static Map<String, ConsensoStrategy> strategies;

    public ConsensoFactory(@Qualifier("ABSOLUTA") ConsensoStrategy absoluta,
                                     @Qualifier("MAYORIA") ConsensoStrategy mayoria,
                           @Qualifier("MULTIPLE_MENCION") ConsensoStrategy multipleMencion) {
        strategies = Map.of(
                "ABSOLUTA", absoluta,
                "MAYORIA", mayoria,
                "MULTIPLE_MENCION", multipleMencion
        );
    }

    static {
        strategies = Map.of(
                "ABSOLUTA", new ConsensoAbsolutaStrategy(),
                "MAYORIA", new ConsensoMayoriaStrategy(),
                "MULTIPLE_MENCION", new ConsensoMultipleMencionStrategy()
        );
    }

    public static ConsensoStrategy getStrategy(String modoNavegacion) {
        return strategies.getOrDefault(modoNavegacion.toUpperCase(), null);
    }
}
