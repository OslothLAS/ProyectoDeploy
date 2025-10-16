package ar.utn.frba.ddsi.agregador.models.entities.solicitudes;

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
@Table(name = "solicitud_eliminacion")
public class SolicitudEliminacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hecho_id", nullable = false)
    private Long idHecho;  // referencia al hecho

    @Column(name = "solicitante", nullable = true)
    private String solicitante;  // username del solicitante

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaDeCreacion;

    @Column(name = "justificacion", nullable = false, length = 1000)
    private String justificacion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "solicitud_id")
    private List<EstadoSolicitud> estados = new ArrayList<>();

    // Constructor principal para crear la solicitud
    public SolicitudEliminacion(String justificacion, Long idHecho, String solicitante) {
        this.justificacion = justificacion;
        this.idHecho = idHecho;
        this.solicitante = solicitante;
        this.fechaDeCreacion = LocalDateTime.now();
        this.estados = new ArrayList<>();

        if (DetectorDeSpam.getInstance().isSpam(justificacion)) {
            this.marcarComoSpam();
        } else {
            this.estados.add(new EstadoSolicitud(null, PosibleEstadoSolicitud.PENDIENTE));
        }
    }

    // Cambia el estado de la solicitud agregando uno nuevo
    public void cambiarEstadoSolicitud(EstadoSolicitud estado) {
        if (!this.estados.isEmpty()) {
            this.estados.get(this.estados.size() - 1).setFechaDeCambio(LocalDateTime.now());
        }
        this.estados.add(estado);
    }

    // Marca la solicitud como spam
    public void marcarComoSpam() {
        this.estados.add(new EstadoSolicitud(null, PosibleEstadoSolicitud.RECHAZADA));
        this.estados.get(this.estados.size() - 1).setSpam(true);
    }
}
