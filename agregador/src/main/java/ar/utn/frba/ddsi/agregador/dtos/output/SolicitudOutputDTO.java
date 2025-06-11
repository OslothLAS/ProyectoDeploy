package ar.utn.frba.ddsi.agregador.dtos.output;

import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitud;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.IDetectorDeSpam;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Contribuyente;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SolicitudOutputDTO {

    private Long id;
    private Contribuyente solicitante;
    private LocalDateTime fechaDeCreacion;
    private LocalDateTime fechaDeEvaluacion;
    private String justificacion;
    private EstadoSolicitudEliminacion estado;
    private List<EstadoSolicitud> historialDeSolicitud;
    private Long idHecho;


}
