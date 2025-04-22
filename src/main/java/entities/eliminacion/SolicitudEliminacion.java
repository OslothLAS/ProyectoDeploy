package entities.eliminacion;

import entities.hechos.Hecho;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
public class SolicitudEliminacion {
    private Contribuyente solicitante;
    private LocalDateTime fechaDeCreacion;
    @Setter
    private LocalDateTime fechaDeEvaluacion;
    private String justificacion;
    private EstadoSolicitudEliminacion estado;
    private Hecho hecho;



    public String justificarSolicitud(String justificacionSolicitud) {
        if (justificacionSolicitud == null || justificacionSolicitud.length() < 500) {
            throw new IllegalArgumentException("La justificacion debe tener al menos 500 caracteres");
        }
        else{
            this.justificacion = justificacionSolicitud;
        }
        return justificacionSolicitud;
    }

    public SolicitudEliminacion(String justificacion, Hecho hecho, Contribuyente solicitante) {
        this.justificacion = this.justificarSolicitud(justificacion);
        this.estado = EstadoSolicitudEliminacion.PENDIENTE;
        this.hecho = hecho;
        this.solicitante = solicitante;
        this.fechaDeCreacion = LocalDateTime.now();
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
