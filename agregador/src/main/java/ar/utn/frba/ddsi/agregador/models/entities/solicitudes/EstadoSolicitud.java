package ar.utn.frba.ddsi.agregador.models.entities.solicitudes;


import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name ="estadoSolicitud")
public class EstadoSolicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evaluador_id")
    private Usuario evaluador;

    @Enumerated(EnumType.STRING)
    private PosibleEstadoSolicitud estado;

    @Column(name = "fecha_evaluacion")
    private LocalDateTime fechaDeCambio;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaDeCreacion;

    @Column(name = "spam")
    private Boolean spam;

    public EstadoSolicitud(Usuario administrador, PosibleEstadoSolicitud estado) {
        this.evaluador = administrador;
        this.estado = estado;
        this.fechaDeCreacion = LocalDateTime.now();
    }

    private Long calcularTiempoDeRespuesta(LocalDateTime fecha1, LocalDateTime fecha2) {
        Duration duration = Duration.between(fecha1, fecha2);
        return duration.toHours();
    }
}

