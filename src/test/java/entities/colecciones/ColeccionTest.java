package entities.colecciones;

import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.criteriosDePertenencia.CriterioPorCategoria;
import entities.fuentes.Importador;
import entities.hechos.Hecho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class ColeccionTest {

    @Test
    void filtrarHechos() {
        Importador importador = new Importador();
        CriterioPorCategoria criterio1 = new CriterioPorCategoria("caída de aeronave");

        List<CriterioDePertenencia> listaCriterios = new ArrayList<>();
        listaCriterios.add(criterio1);

        Coleccion coleccion = new Coleccion("Colección prueba 1.2", "Esto es una prueba", importador, listaCriterios);

        var listaHechos = importador.obtenerHechos();

        List<Hecho> hechosFiltrados = listaHechos.stream().filter(coleccion::cumpleCriterios).toList();
        hechosFiltrados.forEach(hecho -> hecho.addColeccion(coleccion));

        System.out.println(hechosFiltrados);
        System.out.println(hechosFiltrados.get(0).getColecciones());

        Assertions.assertNotEquals(new ArrayList<>(),hechosFiltrados.get(0).getColecciones());
    }
}