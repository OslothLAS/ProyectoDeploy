package entities.colecciones;

import entities.fuentes.FuenteEstatica;
import entities.hechos.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class Coleccion {
    private String titulo;
    private String descripcion;
    private FuenteEstatica fuente;
    private Map<String, Hecho> hechos;

    @Setter //todavia no sabemos bien esto...
    private CriterioPertenencia criterioPertenencia;

    public Coleccion(String titulo, String descripcion, FuenteEstatica fuente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuente = fuente;
        this.hechos = fuente.obtenerHechos();
    }

    private Map<String,Hecho> obtenerHechoDeFuente(){
        return this.fuente.obtenerHechos();
    }

}