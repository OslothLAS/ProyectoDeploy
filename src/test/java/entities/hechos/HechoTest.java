package entities.hechos;

import entities.fuentes.FuenteEstatica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class HechoTest {

    @Test
    void testToString() {
        FuenteEstatica fuente = new FuenteEstatica("config.properties");
        var hechos = fuente.obtenerHechos();
        Hecho hecho1 = hechos.get("Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones");

        System.out.println(hecho1.toString());

        Assertions.assertNotNull(hecho1);
    }
}