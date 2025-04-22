package entities.fuentes;

import entities.hechos.Hecho;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FuenteEstaticaTest {
/*    GENTE PARA CUANDO VAYAN A PROBAR ESTOS TEST, CAMBIEN EL PATH DE LOS CSV's
      POR LOS QUE TIENEN EN SUS PC's, porque si no, les va a tirar error...      */

    @Test
    @DisplayName("puede leer correctamente el CSV y muestra los atributos del primer hecho")
    public void obtenerHechos(){
        FuenteEstatica fuente = new FuenteEstatica();
        var hechos = fuente.obtenerHechos();

        //la clave del map es el titulo para que no haya repetidos
        Hecho hecho1 = hechos.values().stream().findFirst().get();

        System.out.println("titulo: " + hecho1.getDatosHechos().getTitulo() + "\n" );
        System.out.println("descripcion: " + hecho1.getDatosHechos().getDescripcion() + "\n");
        System.out.println("categoria: " + hecho1.getDatosHechos().getCategoria() + "\n");
        System.out.println("fecha: " + hecho1.getDatosHechos().getFechaHecho() + "\n");
        //no muestro la Ubicacion porque da paja poner getters en la clase Ubicacion

        Assertions.assertNotNull(hechos);
    }

    @Test
    @DisplayName("fuenteEstatica tiene 2 archivos CSV y es capaz de mostrar todos los hechos")
    public void obtenerHechos2(){
        FuenteEstatica fuente = new FuenteEstatica();

        var hechos = fuente.obtenerHechos();

        var listHechos = hechos.values();
        listHechos.forEach(h -> System.out.println(h.getDatosHechos().getTitulo()));
        Assertions.assertNotNull(hechos);
    }
}