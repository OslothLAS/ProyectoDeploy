package entities.colecciones;

import entities.colecciones.consenso.strategies.AlgoritmoConsensoConverter;
import entities.colecciones.consenso.strategies.IAlgoritmoConsenso;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "coleccion")
public class Coleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    @ManyToMany(cascade =  CascadeType.ALL)
    @JoinTable(name = "fuentes_coleccion",
        joinColumns = @JoinColumn(name = "coleccion_id"),
        inverseJoinColumns = @JoinColumn(name = "fuente_id")
    )
    private List<Fuente> importadores;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "coleccion_id")
    private List<CriterioDePertenencia> criteriosDePertenencia;

    @Embedded
    private Handle handle;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaYHoraDeActualizacion;

    @Convert(converter = AlgoritmoConsensoConverter.class) //testear!!
    private IAlgoritmoConsenso consenso;

    public Coleccion(String titulo, String descripcion, List<Fuente> importadores, List<CriterioDePertenencia> criteriosDePertenencia, IAlgoritmoConsenso consenso) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.importadores = importadores;
        this.criteriosDePertenencia = criteriosDePertenencia;
        this.handle = new Handle();
        this.fechaYHoraDeActualizacion = LocalDateTime.now();
        this.consenso = consenso;
    }

    public Coleccion(String titulo, String descripcion, List<Fuente> importadores, List<CriterioDePertenencia> criteriosDePertenencia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.importadores = importadores;
        this.criteriosDePertenencia = criteriosDePertenencia;
        this.handle = new Handle();
        this.fechaYHoraDeActualizacion = LocalDateTime.now();
    }



    // el criterio de pertenencia es el encargado de saber si el hecho cumple o no el mismo criterio, esta bien??!
   /* public void filtrarHechos(List <Hecho> listaHechos) {
        List<Hecho> hechosFiltrados = listaHechos.stream().filter(this::cumpleCriterios).toList();
        hechosFiltrados.forEach(hecho -> hecho.addColeccion(this.getHandle()));
    }*/

    public Boolean cumpleCriterios(Hecho hecho){
        return this.criteriosDePertenencia.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }

    public void setCriteriosDePertenencia(List<CriterioDePertenencia> criterios) {
        criteriosDePertenencia.addAll(criterios);
        /*List<Hecho> todosLosHechos = importadores.stream()
                .flatMap(importador -> importador.obtenerHechos().stream())
                .toList();
        this.filtrarHechos(todosLosHechos)*/
    }

    public void agregarImportador(Fuente fuente) {
        this.importadores.add(fuente);
    }

}
