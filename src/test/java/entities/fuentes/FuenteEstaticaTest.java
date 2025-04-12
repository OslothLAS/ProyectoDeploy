package entities.fuentes;

import entities.hechos.Hecho1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FuenteEstaticaTest {
    @Test
    @DisplayName("puede leer correctamente el CSV y muestra los atributos del primer hecho")
    public void obtenerHechos(){
        FuenteEstatica fuente = new FuenteEstatica();
        var hechos = fuente.obtenerHechos("/home/fran/diseño/datasets/pruebaHechos.csv");

        //la clave del map es el titulo para que no haya repetidos
        Hecho1 hecho1 = hechos.get("Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones");

        System.out.println(hecho1.getTitulo() );
        System.out.println(hecho1.getDescripcion());
        System.out.println(hecho1.getCategoria());
        System.out.println(hecho1.getFechaHecho());
        //no muestro la Ubicacion porque da paja poner getters en la clase Ubicacion

        Assertions.assertNotNull(hechos);
    }
}