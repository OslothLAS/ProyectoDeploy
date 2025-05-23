package models.entities.colecciones;

import lombok.Getter;
import models.entities.criteriosDePertenencia.CriterioDePertenencia;
import models.entities.fuentes.Importador;
import models.entities.hechos.Hecho;

import java.util.List;

@Getter
public class Coleccion {
    private final String titulo;
    private final String descripcion;
    //lista de importadores ahora
    private final List<Importador> importadores;
    private List<CriterioDePertenencia> criteriosDePertenencia;

    public Coleccion(String titulo, String descripcion, List<Importador> importadores, List<CriterioDePertenencia> criteriosDePertenencia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.importadores = importadores;
        this.criteriosDePertenencia = criteriosDePertenencia;
    }

    // el criterio de pertenencia es el encargado de saber si el hecho cumple o no el mismo criterio, esta bien??!
    public void filtrarHechos(List<Hecho> listaHechos) {
        List<Hecho> hechosFiltrados = listaHechos.stream().filter(this::cumpleCriterios).toList();
        hechosFiltrados.forEach(hecho -> hecho.addColeccion(this));
    }

    public Boolean cumpleCriterios(Hecho hecho){
        return this.criteriosDePertenencia.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }

    public void setCriteriosDePertenencia(List<CriterioDePertenencia> criterios) {
        criteriosDePertenencia.addAll(criterios);
        this.filtrarHechos(importadores.stream()
                .flatMap(importador -> importador.obtenerHechos().stream())
                .toList());
    }

}
