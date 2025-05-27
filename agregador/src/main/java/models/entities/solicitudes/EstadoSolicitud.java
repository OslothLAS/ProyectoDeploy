package models.entities.solicitudes;

import models.entities.usuarios.Administrador;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.*;
@Getter
public class EstadoSolicitud {
// hola
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
