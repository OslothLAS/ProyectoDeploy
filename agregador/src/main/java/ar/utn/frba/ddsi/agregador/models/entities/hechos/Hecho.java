package ar.utn.frba.ddsi.agregador.models.entities.hechos;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Handle;
import ar.utn.frba.ddsi.agregador.models.entities.normalizador.NormalizadorHecho;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hecho_id")
    private List<Multimedia> multimedia;

    @Builder.Default //falta el atributo en db
    private List<String> etiquetas = new ArrayList<>();

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(name = "fecha")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHecho;

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

    public static Hecho create(){
        return Hecho.builder()
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .origen(Origen.DATASET)
                .colecciones(new ArrayList<>())
                .esConsensuado(false)
                .build();
    }

    public static Hecho create(List<Coleccion> colecciones , List<Handle> handles, Boolean esConsensuado) {
        return Hecho.builder()
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .origen(Origen.EXTERNO)
                .handles(handles)
                .colecciones(colecciones)
                .esConsensuado(esConsensuado)
                .build();
    }

    public static Hecho create(List<Coleccion> colecciones, Boolean esConsensuado) {
        return Hecho.builder()
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .origen(Origen.EXTERNO)
                .colecciones(colecciones)
                .esConsensuado(esConsensuado)
                .build();
    }

    //creacion con multimedia anonima
    public static Hecho create(List<Multimedia> multimedia) {
        return Hecho.builder()
                .esValido(true)
                .multimedia(multimedia)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .colecciones(new ArrayList<>())
                .esConsensuado(false)
                .build();
    }
    //creacion con multimedia registrado (multimedia puede ser null tranquilamente)
    public static Hecho create(Usuario usuario, List<Multimedia> multimedia, Boolean mostrarDatos) {
        return Hecho.builder()
                .esValido(true)
                .autor(usuario)
                .multimedia(multimedia)
                .etiquetas(new ArrayList<>())
                .mostrarDatos(mostrarDatos)
                .fechaCreacion(LocalDateTime.now())
                .origen(Origen.CONTRIBUYENTE)
                .colecciones(new ArrayList<>())
                .esConsensuado(false)
                .build();
    }


    public void addEtiqueta(String etiqueta) {
        this.etiquetas.add(etiqueta);
    }

    public void addColeccion(Coleccion coleccion) {
        if (this.colecciones == null) {
            this.colecciones = new ArrayList<>();
        }
        this.colecciones.add(coleccion);
    }

    public Boolean esEditable() {
        if (!this.esEditable) {
            return false;
        }
        LocalDateTime fechaLimite = this.fechaCreacion.plus(this.plazoEdicion);
        return LocalDateTime.now().isBefore(fechaLimite);
    }

    public List<String> getTituloYDescripcion(){
        String titulo = this.getTitulo();
        String descripcion = this.getDescripcion();
        List<String> tituloYdesc = new ArrayList<>();
        tituloYdesc.add(titulo);
        tituloYdesc.add(descripcion);

        return tituloYdesc;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hecho that = (Hecho) o;

        return Objects.equals(titulo, that.getTitulo()) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(categoria, that.categoria) &&
                Objects.equals(ubicacion, that.ubicacion) &&
                Objects.equals(fechaHecho, that.fechaHecho);
    }
    @Override
    public int hashCode() {
        return Objects.hash(titulo, descripcion, categoria, ubicacion, fechaHecho);
    }

    public String toString() {
        return "DatosHechos{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", ubicacion=" + ubicacion +
                ", fechaHecho=" + fechaHecho +
                '}';
    }

    public void normalizarHecho(){
        Categoria categoria = this.getCategoria();
        categoria.setCategoria(NormalizadorHecho.
                normalizarCategoria(this.getCategoria().getCategoria()));

        Ubicacion ubicacion = this.getUbicacion();
        double latitud = Double.parseDouble(ubicacion.getLatitud());
        double longitud =  Double.parseDouble(ubicacion.getLongitud());
        Provincia provincia = null;
        Localidad localidad = null;

        if (ubicacion.getLocalidad() != null) {
            localidad = ubicacion.getLocalidad();
            if (localidad.getProvincia() != null) {
                provincia = localidad.getProvincia();
            }
        }
        if (provincia == null) {
            provincia = new Provincia();
        }

        List<String> resultados = NormalizadorHecho.normalizarUbicacion(latitud,longitud);
        provincia.setNombre(resultados.get(0));

        if (localidad == null) {
            localidad = new Localidad();
            localidad.setProvincia(provincia);
            ubicacion.setLocalidad(localidad);
        } else {
            localidad.setNombre(resultados.get(1));
            localidad.setProvincia(provincia);
        }
    }

}