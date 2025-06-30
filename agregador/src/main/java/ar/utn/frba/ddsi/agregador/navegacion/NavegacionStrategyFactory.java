package ar.utn.frba.ddsi.agregador.navegacion;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NavegacionStrategyFactory {
    private final Map<String, NavegacionStrategy> strategies;

    public NavegacionStrategyFactory(@Qualifier("IRRESTRICTO") NavegacionStrategy irrestricto,
                                     @Qualifier("CURADO") NavegacionStrategy curado) {
        this.strategies = Map.of(
                "IRRESTRICTO", irrestricto,
                "CURADO", curado
        );
    }

    public NavegacionStrategy getStrategy(String modoNavegacion) {
        return strategies.getOrDefault(modoNavegacion.toUpperCase(), strategies.get("IRRESTRICTO"));
    }
}
