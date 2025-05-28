package ar.utn.frba.ddsi.agregador.services.impl;

import entities.colecciones.Fuente;
import entities.hechos.Hecho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColeccionServiceTest {

    @Test
    public void tomarHechosImportadores(){
        Fuente fuenteEstatica = new Fuente("localhost","8060");
        //Fuente fuenteDinamica = new Fuente("localhost","8070");
        //Fuente fuenteProxy = new Fuente("localhost","8090");

        List<Fuente> importadores = List.of(fuenteEstatica);

        List<Hecho> hechos = importadores.stream()
                .flatMap(i -> i.obtenerHechos().stream())
                .toList();

        Hecho hecho1 = hechos.get(0);
        System.out.println(hecho1.getDatosHechos().getTitulo());
        System.out.println(hecho1.getDatosHechos().getDescripcion());

        Assertions.assertNotNull(hechos);
    }
}