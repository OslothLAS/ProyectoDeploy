package entities.solicitudes;

import entities.hechos.Hecho;
import entities.usuarios.Contribuyente;
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

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Contribuyente solicitante;

    private LocalDateTime fechaDeCreacion;
    private LocalDateTime fechaDeEvaluacion;

    private String justificacion;

    @OneToMany
    private List<EstadoSolicitud> estados;

    public SolicitudEliminacion(String justificacion,Hecho hecho, Contribuyente solicitante) {
        this.justificacion = justificacion;
        this.hecho = hecho;
        this.solicitante = solicitante;
        this.fechaDeCreacion = LocalDateTime.now();
        this.estados = new ArrayList<>();
        if(DetectorDeSpam.getInstance().isSpam(justificacion)) {
            this.estados.add(new EstadoSolicitud(null, PosibleEstadoSolicitud.RECHAZADA));
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

    public PosibleEstadoSolicitud getEstado() {
        return this.estados.get(estados.size() - 1).getEstado();
    }
}
