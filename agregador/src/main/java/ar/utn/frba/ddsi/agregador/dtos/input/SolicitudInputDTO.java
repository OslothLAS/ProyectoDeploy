package ar.utn.frba.ddsi.agregador.dtos.input;

import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitud;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.IDetectorDeSpam;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Contribuyente;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SolicitudInputDTO {
    private Long id;
    private Contribuyente solicitante;
    private String justificacion;

}