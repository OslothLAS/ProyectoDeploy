package entities.eliminacion;

import entities.usuarios.Administrador;
import entities.usuarios.Contribuyente;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.*;
@Getter
public class EstadoSolicitud {

    private Administrador administrador;
    private Long tiempoDeRespuesta;

    public void guardarEstado(Administrador administrador, SolicitudEliminacion solicitud){
        this.administrador = administrador;
        this.tiempoDeRespuesta = calcularTiempoDeRespuesta(
                solicitud.getFechaDeCreacion(),
                solicitud.getFechaDeEvaluacion());
    }


    public Long calcularTiempoDeRespuesta(LocalDateTime fecha1, LocalDateTime fecha2) {
        Duration duration = Duration.between(fecha1, fecha2);
        return duration.toHours();
    }

}


