package entities.colecciones;

import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.Importador;
import entities.hechos.Hecho;
import lombok.Getter;
import java.util.List;


@Getter
public class Coleccion {
    private final String titulo;
    private final String descripcion;
    private final List<Fuente> importadores;
    private List<CriterioDePertenencia> criteriosDePertenencia;
    private final Handle handle;

    public Coleccion(String titulo, String descripcion, List<Fuente> importadores, List<CriterioDePertenencia> criteriosDePertenencia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.importadores = importadores;
        this.criteriosDePertenencia = criteriosDePertenencia;
        this.handle = new Handle();
    }

    // el criterio de pertenencia es el encargado de saber si el hecho cumple o no el mismo criterio, esta bien??!
    public void filtrarHechos(List <Hecho> listaHechos) {
        List<Hecho> hechosFiltrados = listaHechos.stream().filter(this::cumpleCriterios).toList();
        hechosFiltrados.forEach(hecho -> hecho.addColeccion(this));
    }

    public Boolean cumpleCriterios(Hecho hecho){
        return this.criteriosDePertenencia.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }

    public void setCriteriosDePertenencia(List<CriterioDePertenencia> criterios) {
        criteriosDePertenencia.addAll(criterios);
        List<Hecho> todosLosHechos = importadores.stream()
                .flatMap(importador -> importador.obtenerHechos().stream())
                .toList();
        this.filtrarHechos(todosLosHechos);
    }

}
