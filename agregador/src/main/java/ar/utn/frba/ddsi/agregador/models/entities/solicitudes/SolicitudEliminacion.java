package ar.utn.frba.ddsi.agregador.models.entities.solicitudes;

import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Administrador;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Contribuyente;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor

@Getter
public class SolicitudEliminacion {
    @Setter
    private Long id;
    private Contribuyente solicitante;
    private LocalDateTime fechaDeCreacion;
    @Setter
    private LocalDateTime fechaDeEvaluacion;
    private String justificacion;
    private EstadoSolicitudEliminacion estado;
    private List<EstadoSolicitud> historialDeSolicitud;
    private Long idHecho;
    @Setter
    private IDetectorDeSpam detectorDeSpam = new DetectorDeSpam();

    public SolicitudEliminacion(String justificacion, Long idHecho, Contribuyente solicitante) {
        this.justificacion = justificacion;
        this.solicitante = solicitante;
        this.fechaDeCreacion = LocalDateTime.now();
        if(detectorDeSpam.isSpam(justificacion)){
            this.estado = EstadoSolicitudEliminacion.RECHAZADA;}
        else{
            this.estado = EstadoSolicitudEliminacion.PENDIENTE;
        }
        this.idHecho = idHecho;
        this.historialDeSolicitud = new ArrayList<>();
    }


    public void cambiarEstadoSolicitud(EstadoSolicitudEliminacion estado) {
        this.estado = EstadoSolicitudEliminacion.valueOf(estado.name());
        this.fechaDeEvaluacion = LocalDateTime.now();
    }

    public void actualizarHistorialDeOperacion(EstadoSolicitudEliminacion estado, Administrador admin){
            EstadoSolicitud estadoSolicitud = new EstadoSolicitud();
        estadoSolicitud.guardarEstado(estado, admin, this);
        this.historialDeSolicitud.add(estadoSolicitud);
    }
}