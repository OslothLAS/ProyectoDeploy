package ar.utn.frba.ddsi.agregador.dtos.output;

import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.PosibleEstadoSolicitud;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EstadoSolicitudDTO {

    private Long id;
    private String evaluador; // username del evaluador
    private PosibleEstadoSolicitud estado;
    private LocalDateTime fechaDeCambio; // fecha de evaluación
    private LocalDateTime fechaDeCreacion; // cuando se creó el estado
    private Boolean spam;

    // Constructor vacío
    public EstadoSolicitudDTO() {
    }

    // Constructor a partir de la entidad
    public EstadoSolicitudDTO(ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitud estadoSolicitud) {
        this.id = estadoSolicitud.getId();
        this.evaluador = estadoSolicitud.getEvaluador();
        this.estado = estadoSolicitud.getEstado();
        this.fechaDeCambio = estadoSolicitud.getFechaDeCambio();
        this.fechaDeCreacion = estadoSolicitud.getFechaDeCreacion();
        this.spam = estadoSolicitud.getSpam();
    }
}
