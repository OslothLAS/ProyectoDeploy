package ar.utn.ba.ddsi.fuenteDinamica.models.entities.solicitudes;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.usuarios.Administrador;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class EstadoSolicitud {

    private Administrador administrador;
    private Long tiempoDeRespuesta;
    private EstadoSolicitudEliminacion estado;

    public void guardarEstado(EstadoSolicitudEliminacion estado, Administrador administrador, SolicitudEliminacion solicitud){
        this.administrador = administrador;
        this.tiempoDeRespuesta = calcularTiempoDeRespuesta(
                solicitud.getFechaDeCreacion(),
                solicitud.getFechaDeEvaluacion());
        this.estado = estado;
    }


    private Long calcularTiempoDeRespuesta(LocalDateTime fecha1, LocalDateTime fecha2) {
        Duration duration = Duration.between(fecha1, fecha2);
        return duration.toHours();
    }

}
