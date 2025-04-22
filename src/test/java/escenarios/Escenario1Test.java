package escenarios;

import entities.colecciones.Coleccion;
import entities.criteriosDePertenencia.CriterioPorCategoria;
import entities.criteriosDePertenencia.CriterioPorFecha;
import entities.fuentes.FuenteEstatica;
import entities.hechos.Hecho;
import entities.hechos.RepositorioDeHechos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Escenario1Test {
    @Test
    @DisplayName("Escenario 1:  Creación de colección mediante carga manual" +
                 "Como persona administradora, deseo crear una colección")
    public void obtenerHechos2(){
        FuenteEstatica fuente = new FuenteEstatica();

        Coleccion coleccion = new Coleccion("Colección prueba", "Esto es una prueba", fuente);
        var hechos = coleccion.getHechos();
        hechos.values().forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));
        Assertions.assertNotNull(hechos);
    }

    @Test
    @DisplayName("Escenario 1.2: Criterios de pertenencia" +
                 "Como persona administradora, deseo añadir un criterio a la colección")
    public void filtrarHechos(){
        FuenteEstatica fuente = new FuenteEstatica();

        Coleccion coleccion = new Coleccion("Colección prueba 1.2", "Esto es una prueba", fuente);

        CriterioPorFecha criterio1 = new CriterioPorFecha(LocalDate.of(2000, 1, 1), LocalDate.of(2010, 1, 1));
        CriterioPorCategoria criterio2 = new CriterioPorCategoria("Caída de aeronave");

        coleccion.setCriteriosDePertenencia(List.of(criterio1));
        var hechos1 = coleccion.getHechos();
        hechos1.values().forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));

        System.out.println("\n");

        coleccion.setCriteriosDePertenencia(List.of(criterio2));
        var hechos2 = coleccion.getHechos();
        hechos2.values().forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));

        Assertions.assertNotNull(hechos1);

    }

    @Test
    @DisplayName("Escenario 1.3: Filtros del visualizador" +
                 "Como visualizador agrego como filtro unTitulo y me muestra una lista vacía")
    public void visualizarHechosConFiltro() {
        FuenteEstatica fuente = new FuenteEstatica();

        Coleccion coleccion = new Coleccion("Coleccion prueba 1.3","no se que poner aca",fuente);

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
    }

    @Test
    @DisplayName("Escenario 1.4 Etiquetas")
    public void addEtiqueta(){
        FuenteEstatica fuente = new FuenteEstatica();
        Map<String, Hecho> mapHechos = fuente.obtenerHechos();

        Hecho hecho = mapHechos.get("Caída de aeronave impacta en Olavarría");

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


