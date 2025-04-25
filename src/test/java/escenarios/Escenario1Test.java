package escenarios;

import entities.colecciones.Coleccion;
import entities.criteriosDePertenencia.CriterioPorCategoria;
import entities.criteriosDePertenencia.CriterioPorFecha;
import entities.fuentes.Importador;
import entities.hechos.Hecho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class Escenario1Test {
    @Test
    @DisplayName("Escenario 1:  Creación de colección mediante carga manual" +
                 "Como persona administradora, deseo crear una colección")
    public void obtenerHechos2(){
        Importador importador = new Importador();

        Coleccion coleccion = new Coleccion("Colección prueba", "Esto es una prueba", importador);
        var hechos = coleccion.getHechos();
        hechos.forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));
        Assertions.assertNotNull(hechos);
    }

    @Test
    @DisplayName("Escenario 1.2: Criterios de pertenencia" +
                 "Como persona administradora, deseo añadir un criterio a la colección")
    public void filtrarHechos(){
        Importador importador = new Importador();

        Coleccion coleccion = new Coleccion("Colección prueba 1.2", "Esto es una prueba", importador);

        CriterioPorFecha criterio1 = new CriterioPorFecha(LocalDate.of(2000, 1, 1), LocalDate.of(2010, 1, 1));
        CriterioPorCategoria criterio2 = new CriterioPorCategoria("Caída de aeronave");

        coleccion.setCriteriosDePertenencia(List.of(criterio1));
        var hechos1 = coleccion.getHechos();
        hechos1.forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));

        System.out.println("\n");

        coleccion.setCriteriosDePertenencia(List.of(criterio2));
        var hechos2 = coleccion.getHechos();
        hechos2.forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));

        Assertions.assertNotNull(hechos1);

    }

    /*@Test
    @DisplayName("Escenario 1.3: Filtros del visualizador" +
                 "Como visualizador agrego como filtro unTitulo y me muestra una lista vacía")
    public void visualizarHechosConFiltro() {
        Importador importador = new Importador();

        Coleccion coleccion = new Coleccion("Coleccion prueba 1.3","no se que poner aca",importador);

        //esto es como un visualizador puede ver cosas
        RepositorioDeHechos repo = new RepositorioDeHechos();
        Map<String, String> filtros = new HashMap<>();
        filtros.put("Categoria","Caida de aeronave");

        var listHechos1 = repo.visualizarHechosConFiltro(coleccion, filtros);
        listHechos1.forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));
        System.out.println("\nahora se ingresara el filtro 'unTitulo'\n");

        filtros.put("Titulo","UnTitulo");

        var listHechos = repo.visualizarHechosConFiltro(coleccion, filtros);

        listHechos.forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));

        assertTrue(listHechos.isEmpty());
    }*/

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


