package ar.utn.frba.ddsi.agregador.models.entities.hechos;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Handle;
import ar.utn.frba.ddsi.agregador.models.entities.normalizador.NormalizadorHecho;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hecho")
@EqualsAndHashCode(of = {"titulo", "descripcion"})
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "valido")
    private Boolean esValido;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(name = "fecha")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHecho;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    public Hecho(Boolean esValido,
                 String titulo,
                 String descripcion,
                 Categoria cat,
                 Ubicacion ubi,
                 LocalDateTime fechaHecho,
                 List<Multimedia> multimediaNueva,
                 Boolean mostrarDatos,
                 Object o) {
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

    public void normalizarHecho(){
        Categoria categoria = this.getCategoria();
        categoria.setCategoria(NormalizadorHecho.
                normalizarCategoria(this.getCategoria().getCategoria()));

        Ubicacion ubicacion = this.getUbicacion();



        List<Double> latyLongNormalizadas = NormalizadorHecho.normalizarUbicaciones(ubicacion.getLatitud(),ubicacion.getLongitud());
        double latitud = latyLongNormalizadas.get(0);
        double longitud =  latyLongNormalizadas.get(1);

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