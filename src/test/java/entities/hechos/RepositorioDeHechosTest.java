package entities.hechos;

import entities.colecciones.Coleccion;
import entities.fuentes.FuenteEstatica;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class RepositorioDeHechosTest {

    @Test
    void visualizarHechosConFiltro() {
        FuenteEstatica fuente = new FuenteEstatica();

        Coleccion coleccion = new Coleccion("nuevaColeccionJEJE","no se que poner aca",fuente);

        //esto es como un visualizador puede ver cosas
        RepositorioDeHechos repo = new RepositorioDeHechos();
        Map<String, String> filtros = new HashMap<>();
        filtros.put("Categoria","Caida");
        filtros.put("Titulo","UnTitulo");

        var listHechos = repo.visualizarHechosConFiltro(coleccion, filtros);

        listHechos.forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));

        assertTrue(listHechos.isEmpty());
    }
}