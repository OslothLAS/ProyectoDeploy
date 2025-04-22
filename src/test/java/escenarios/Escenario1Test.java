package escenarios;

import entities.colecciones.Coleccion;
import entities.fuentes.FuenteEstatica;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Escenario1Test {
    @Test
    @DisplayName("Como persona administradora, deseo crear una colección")
    public void obtenerHechos2(){
        FuenteEstatica fuente = new FuenteEstatica("C:/Users/Silvia/Desktop/2025-tpa-mi-no-grupo-23/src/main/resources/config.properties");

        Coleccion coleccion = new Coleccion("Colección prueba", "Esto es una prueba", fuente);
        var hechos = coleccion.getHechos();
        hechos.values().forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));
        Assertions.assertNotNull(hechos);
    }
}
