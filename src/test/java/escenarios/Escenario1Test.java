package escenarios;

import entities.colecciones.Coleccion;
import entities.fuentes.Importador;
import entities.hechos.Hecho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class Escenario1Test {
    @Test
    @DisplayName("Escenario 1:  Creación de colección mediante carga manual" +
                 "Como persona administradora, deseo crear una colección")
    public void obtenerHechos2(){
        Importador importador = new Importador();

        Coleccion coleccion = new Coleccion("Colección prueba", "Esto es una prueba", importador, new ArrayList<>());
        var hechos = coleccion.getImportador().obtenerHechos();

        Assertions.assertNotNull(hechos);
    }


    @Test
    @DisplayName("Escenario 1.4 Etiquetas")
    public void addEtiqueta(){
        Importador importador = new Importador();
        List<Hecho> hechos = importador.obtenerHechos();

        Hecho hecho = hechos.get(0);

        hecho.addEtiqueta("Olavarría");
        hecho.addEtiqueta("Grave");

        System.out.println("Etiquetas: " + hecho.getEtiquetas());
        assertAll("el hecho retiene todas las etiquetas",
                () -> assertEquals(2, hecho.getEtiquetas().size(), "Debería tener 2 etiquetas"),
                () -> assertTrue(hecho.getEtiquetas().contains("Olavarría"), "Falta etiqueta 'Olavarría'"),
                () -> assertTrue(hecho.getEtiquetas().contains("Grave"), "Falta etiqueta 'Grave'")
        );
    }

}


