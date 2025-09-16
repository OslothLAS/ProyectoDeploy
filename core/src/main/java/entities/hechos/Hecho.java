package entities.hechos;

import entities.colecciones.Coleccion;
import entities.colecciones.Handle;
import entities.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hecho")
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    @Column(name = "valido")
    private Boolean esValido;

    @Embedded
    private DatosHechos datosHechos;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hecho_id")
    private List<Multimedia> multimedia;

    @Builder.Default //falta el atributo en db
    private List<String> etiquetas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "hecho_coleccion",
            joinColumns = @JoinColumn(name = "hecho_id"),
            inverseJoinColumns = @JoinColumn(name = "coleccion_id")
    )
    private List<Coleccion> colecciones = new ArrayList<>();

    @Transient
    private List<Handle> handles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "origen_carga")
    private Origen origen;

    @Enumerated(EnumType.STRING)
    @Column(name = "origen_fuente")
    private FuenteOrigen fuenteOrigen;

    @Transient
    private Boolean mostrarDatos; //ver esto

    @Column(name = "fecha_carga")
    private LocalDateTime fechaCreacion;

    @Transient //corregir
    private Duration plazoEdicion;

    @Column(name = "editable")
    private Boolean esEditable;

    @Transient
    private Boolean esConsensuado;


    public static Hecho create(DatosHechos datosHechos){
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCarga(LocalDateTime.now())
                .origenCarga(Origen.DATASET)
                .colecciones(new HashMap<>())
                .build();
    }


    public static Hecho create(DatosHechos datosHechos,List<Coleccion> colecciones ,List<Handle> handles, Boolean esConsensuado) {

        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .origen(Origen.EXTERNO)
                .handles(handles)
                .colecciones(colecciones)
                .build();
    }

    public static Hecho create(DatosHechos datosHechos, List<Coleccion> colecciones, Boolean esConsensuado) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .origen(Origen.EXTERNO)
                .colecciones(colecciones)
                .esConsensuado(esConsensuado)
                .build();
    }

    //creacion con multimedia anonima
    public static Hecho create(DatosHechos datosHechos, List<Multimedia> multimedia) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .multimedia(multimedia)
                .etiquetas(new ArrayList<>())
                .fechaCarga(LocalDateTime.now())
                .colecciones(new HashMap<>())
                .build();
    }
    //creacion con multimedia registrado (multimedia puede ser null tranquilamente)
    public static Hecho create(DatosHechos datosHechos, Usuario usuario, List<Multimedia> multimedia, Boolean mostrarDatos) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .autor(usuario)
                .multimedia(multimedia)
                .etiquetas(new ArrayList<>())
                .mostrarDatos(mostrarDatos)
                .fechaCarga(LocalDateTime.now())
                .origenCarga(Origen.CONTRIBUYENTE)
                .colecciones(new HashMap<>())
                .build();
    }


    public void addEtiqueta(String etiqueta) {
            this.etiquetas.add(etiqueta);
    }

    public void addColeccion(Coleccion coleccion) {
        if (this.colecciones == null) {
            this.colecciones = new HashMap<>();
        }
        this.colecciones.put(coleccion, Boolean.TRUE);
    }

    public Boolean esEditable() {
        if (!this.esEditable) {
            return false;
        }
        LocalDateTime fechaLimite = this.fechaCarga.plus(this.plazoEdicion);
        return LocalDateTime.now().isBefore(fechaLimite);
    }

    public List<String> getTituloYDescripcion(){
        String titulo = this.getDatosHechos().getTitulo();
        String descripcion = this.getDatosHechos().getDescripcion();
        List<String> tituloYdesc = new ArrayList<>();
        tituloYdesc.add(titulo);
        tituloYdesc.add(descripcion);

        return tituloYdesc;
    }

    public void normalizarHecho(){
        this.getDatosHechos().normalizarHecho();
    }

    public String getTitulo() {
        return this.getDatosHechos().getTitulo();
    }

    public String getDescripcion() {
        return this.getDatosHechos().getDescripcion();
    }
}
