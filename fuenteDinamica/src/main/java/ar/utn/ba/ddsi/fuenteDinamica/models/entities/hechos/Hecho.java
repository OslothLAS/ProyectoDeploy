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

    @Column(name = "autor_id", nullable = false)
    private Long autorId;

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
    private List<Multimedia> multimedia;

    @Builder.Default //falta el atributo en db
    private List<String> etiquetas = new ArrayList<>();


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

    public void addEtiqueta(String etiqueta) {
        this.etiquetas.add(etiqueta);
    }

    public Boolean esEditable() {
        if (!this.esEditable) {
            return false;
        }
        LocalDateTime fechaLimite = this.fechaCreacion.plus(this.plazoEdicion);
        return LocalDateTime.now().isBefore(fechaLimite);
    }


}