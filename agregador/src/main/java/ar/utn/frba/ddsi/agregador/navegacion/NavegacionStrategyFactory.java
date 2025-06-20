package ar.utn.frba.ddsi.agregador.navegacion;

public class NavegacionStrategyFactory {
    public static NavegacionStrategy getStrategy(String modoNavegacion) {
        if ("irrestricta".equalsIgnoreCase(modoNavegacion)) {
            return new NavegacionIrrestricaStrategy();
        } else {
            return new NavegacionCuradaStrategy();
        }
    }
}
