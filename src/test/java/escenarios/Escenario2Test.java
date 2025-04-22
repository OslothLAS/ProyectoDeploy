package escenarios;

import entities.fuentes.FuenteEstatica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Escenario2Test {
    @Test
    @DisplayName("Obtener hechos de un CSV")
    public void obtenerHechos(){
        FuenteEstatica fuente = new FuenteEstatica();

        var hechos = fuente.obtenerHechos();

        var listHechos = hechos.values();
        listHechos.forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));
        Assertions.assertNotNull(hechos);
    }
}
