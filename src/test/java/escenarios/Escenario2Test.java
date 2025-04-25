package escenarios;

import entities.fuentes.Importador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Escenario2Test {
    @Test
    @DisplayName("Obtener hechos de un CSV")
    public void obtenerHechos(){
        Importador fuente = new Importador();

        var hechos = fuente.obtenerHechos();

        hechos.forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));
        Assertions.assertNotNull(hechos);
    }
}
