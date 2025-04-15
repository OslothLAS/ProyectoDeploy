package entities.fuentes;

import entities.hechos.Hecho;


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
        Hecho hecho1 = hechos.get("Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones");

        System.out.println("titulo: " + hecho1.getTitulo() + "\n" );
        System.out.println("descripcion: " + hecho1.getDescripcion() + "\n");
        System.out.println("categoria: " + hecho1.getCategoria() + "\n");
        System.out.println("fecha: " + hecho1.getFechaHecho() + "\n");
        //no muestro la Ubicacion porque da paja poner getters en la clase Ubicacion

        Assertions.assertNotNull(hechos);
    }
}