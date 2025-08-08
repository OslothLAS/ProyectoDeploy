package entities.colecciones;

import entities.colecciones.consenso.strategies.IAlgoritmoConsenso;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.hechos.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class Coleccion {
    private Long id;
    private final String titulo;
    private final String descripcion;
    private final List<Fuente> fuentes;
    private List<CriterioDePertenencia> criteriosDePertenencia;
    private final Handle handle;
    private LocalDateTime fechaYHoraDeActualizacion;
    private IAlgoritmoConsenso consenso;

    public Coleccion(String titulo, String descripcion, List<Fuente> fuentes, List<CriterioDePertenencia> criteriosDePertenencia, IAlgoritmoConsenso consenso) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuentes = fuentes;
        this.criteriosDePertenencia = criteriosDePertenencia;
        this.handle = new Handle();
        this.fechaYHoraDeActualizacion = LocalDateTime.now();
        this.consenso = consenso;
    }

    public Coleccion(String titulo, String descripcion, List<Fuente> fuentes, List<CriterioDePertenencia> criteriosDePertenencia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuentes = fuentes;
        this.criteriosDePertenencia = criteriosDePertenencia;
        this.handle = new Handle();
        this.fechaYHoraDeActualizacion = LocalDateTime.now();
    }

    // el criterio de pertenencia es el encargado de saber si el hecho cumple o no el mismo criterio, esta bien??!
    public void filtrarHechos(List <Hecho> listaHechos) {
        List<Hecho> hechosFiltrados = listaHechos.stream().filter(this::cumpleCriterios).toList();
        hechosFiltrados.forEach(hecho -> hecho.addColeccion(this.getHandle()));
    }

    public Boolean cumpleCriterios(Hecho hecho){
        return this.criteriosDePertenencia.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }

    public void setCriteriosDePertenencia(List<CriterioDePertenencia> criterios) {
        criteriosDePertenencia.addAll(criterios);
    }

    public void agregarFuente(Fuente fuente) {
        this.fuentes.add(fuente);
    }

    public void quitarFuente(Fuente fuente) {
        this.fuentes.remove(fuente);
    }
}
