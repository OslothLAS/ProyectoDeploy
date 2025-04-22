package entities.eliminacion;

import entities.hechos.Hecho;
import lombok.*;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Getter
public class SolicitudEliminacion {
    private Contribuyente solicitante;
    private LocalDateTime fechaDeCreacion;

    @Setter
    private LocalDateTime fechaDeEvaluacion;
    private String justificacion;
    private EstadoSolicitudEliminacion estado;
    private Hecho hecho;

    public static SolicitudEliminacion create(String justificacion, Hecho hecho, Contribuyente solicitante){
        return SolicitudEliminacion.builder()
                .justificacion(justificarSolicitud(justificacion))
                .estado(EstadoSolicitudEliminacion.PENDIENTE)
                .hecho(hecho)
                .solicitante(solicitante)
                .fechaDeCreacion(LocalDateTime.now())
                .build();

    }

    /*public SolicitudEliminacion(String justificacion, Hecho hecho, Contribuyente solicitante) {
        this.justificacion = this.justificarSolicitud(justificacion);
        this.estado = EstadoSolicitudEliminacion.PENDIENTE;
        this.hecho = hecho;
        this.solicitante = solicitante;
        this.fechaDeCreacion = LocalDateTime.now();
    }*/

    public static String justificarSolicitud(String justificacionSolicitud) {
        if (justificacionSolicitud == null || justificacionSolicitud.length() < 500) {
            throw new IllegalArgumentException("La justificacion debe tener al menos 500 caracteres");
        }
        else{
            return justificacionSolicitud;
        }

    }

    public void cambiarEstadoHecho(EstadoSolicitudEliminacion estado) {
        if(estado == EstadoSolicitudEliminacion.RECHAZADA) {
            cambiarEstadoSolicitud(estado);
        }
        else if(estado == EstadoSolicitudEliminacion.ACEPTADA){
            cambiarEstadoSolicitud(estado);
            hecho.setEsValido(false);
        }
    }

    private void cambiarEstadoSolicitud(EstadoSolicitudEliminacion estado) {
        this.estado = EstadoSolicitudEliminacion.valueOf(estado.name());
        this.fechaDeEvaluacion = LocalDateTime.now();
    }

}
