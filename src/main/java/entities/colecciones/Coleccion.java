package entities.colecciones;

import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.fuentes.FuenteEstatica;
import entities.hechos.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Coleccion {
    private String titulo;
    private String descripcion;
    private FuenteEstatica fuente;
    private Map<String, Hecho> hechos;
    @Setter
    private List<CriterioDePertenencia> criteriosDePertenencia = new ArrayList<>();

    public Coleccion(String titulo, String descripcion, FuenteEstatica fuente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuente = fuente;
        this.hechos = fuente.obtenerHechos();
    }

}