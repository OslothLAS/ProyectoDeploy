package entities.hechos;

import entities.fuentes.FuenteEstatica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HechoTest {

    @Test
    void testToString() {
        String[] paths = {"/home/fran/diseño/datasets/pruebaHechos.csv"};
        FuenteEstatica fuente = new FuenteEstatica(paths);
        var hechos = fuente.obtenerHechos();
        Hecho hecho1 = hechos.get("Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones");

        System.out.println(hecho1.toString());

        Assertions.assertNotNull(hecho1);
    }
}