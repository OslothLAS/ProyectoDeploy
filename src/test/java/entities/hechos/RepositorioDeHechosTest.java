package entities.hechos;

import entities.colecciones.Coleccion;
import entities.fuentes.FuenteEstatica;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class RepositorioDeHechosTest {

    @Test
    void visualizarHechosConFiltro() {
        FuenteEstatica fuente = new FuenteEstatica("config.properties");

        Coleccion coleccion = new Coleccion("nuevaColeccionJEJE","no se que poner aca",fuente);

        //esto es como un visualizador puede ver cosas
        RepositorioDeHechos repo = new RepositorioDeHechos();
        var listHechos = repo.visualizarHechosConFiltro(coleccion,"emergencia");

        System.out.println(listHechos);

        assertFalse(listHechos.isEmpty());
    }
}