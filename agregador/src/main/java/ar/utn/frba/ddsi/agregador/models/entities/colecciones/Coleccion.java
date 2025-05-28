package ar.utn.frba.ddsi.agregador.models.entities.colecciones;

import lombok.Getter;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.frba.ddsi.agregador.models.entities.fuentes.Importador;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;

import java.util.List;

@Getter
public class Coleccion {
    private final String titulo;
    private final String descripcion;
    private final Handle handle; // Ahora es un objeto Handle
    private final List<Importador> importadores;
    private List<CriterioDePertenencia> criteriosDePertenencia;

    // Constructor que genera automáticamente el handle
    public Coleccion(String titulo, String descripcion, List<Importador> importadores, List<CriterioDePertenencia> criteriosDePertenencia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.handle = new Handle(); // Genera un handle único automáticamente
        this.importadores = importadores;
        this.criteriosDePertenencia = criteriosDePertenencia;
    }

    //--------------------------------------------------------------------------------------------------
    // el criterio de pertenencia es el encargado de saber si el hecho cumple o no el mismo criterio, esta bien??!
    // ESTE METODO TAMBIEN AGREGA LA COLECCION ACTUAL A LOS HECHOS
    //Le asignamos una lista de hechos y trabaja con eso
    //--------------------------------------------------------------------------------------------------
    public void filtrarHechos(List<Hecho> listaHechos) {

        //Toma de la lista de hechos que le dimos para trabajar y filtra las que cumplen con
        //los criterios de pertenencia de ESTA COLECCION
        List<Hecho> hechosFiltrados = listaHechos.stream().filter(this::cumpleCriterios).toList();

        //A la lista de los hechos, se le asigna la coleccion actual
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
