package ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
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

    @ManyToOne
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
    private List<Multimedia> multimedia = new ArrayList<>();

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
    private Duration plazoEdicion = Duration.ofDays(7);

    @Column(name = "editable")
    private Boolean esEditable;

    public Boolean esEditable() {
        if (!this.esEditable) {
            return false;
        }
        LocalDateTime fechaLimite = this.fechaCreacion.plus(this.plazoEdicion);
        return LocalDateTime.now().isBefore(fechaLimite);
    }

    public Hecho(String titulo, String descripcion, Categoria categoria, Ubicacion ubi, LocalDateTime fechaHecho,
                 List <Multimedia> multimedia, Boolean mostrarDatos, Boolean esEditable) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubi;
        this.fechaHecho = fechaHecho;
        this.multimedia = multimedia;
        this.fuenteOrigen = FuenteOrigen.DINAMICO;
        this.mostrarDatos = mostrarDatos;
        this.fechaCreacion = LocalDateTime.now();
        this.esEditable = esEditable;
    }
}