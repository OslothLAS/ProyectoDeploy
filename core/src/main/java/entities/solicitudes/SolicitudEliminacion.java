package entities.solicitudes;

import entities.hechos.Hecho;
import entities.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table( name ="solicitud_eliminacion")
public class SolicitudEliminacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hecho_id")
    private Hecho hecho;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario solicitante;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaDeCreacion;

    @Column(name = "justificacion")
    private String justificacion;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "solicitud_id")
    private List<EstadoSolicitud> estados;

    public SolicitudEliminacion(String justificacion,Hecho hecho, Usuario solicitante) {
        this.justificacion = justificacion;
        this.hecho = hecho;
        this.solicitante = solicitante;
        this.fechaDeCreacion = LocalDateTime.now();
        this.estados = new ArrayList<>();
        if(DetectorDeSpam.getInstance().isSpam(justificacion)) {
            this.marcarComoSpam();
        }else{
            this.estados.add(new EstadoSolicitud(null,PosibleEstadoSolicitud.PENDIENTE));
        }
    }

    public void cambiarEstadoSolicitud(EstadoSolicitud estado) {
        if (!this.estados.isEmpty()) {
            this.estados.get(this.estados.size() - 1).setFechaDeCambio(LocalDateTime.now());
        }
        this.estados.add(estado);
    }

    public void marcarComoSpam() {
        this.estados.add(new EstadoSolicitud(null, PosibleEstadoSolicitud.RECHAZADA));
        this.estados.get(this.estados.size() - 1).setSpam(true);
    }
}
