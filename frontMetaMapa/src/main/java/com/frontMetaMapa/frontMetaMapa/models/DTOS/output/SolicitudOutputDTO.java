package ar.utn.frba.ddsi.agregador.dtos.output;

import entities.solicitudes.EstadoSolicitud;
import entities.usuarios.Usuario;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SolicitudOutputDTO {

    private Long id;
    private Usuario solicitante;
    private LocalDateTime fechaDeCreacion;
    private LocalDateTime fechaDeEvaluacion;
    private String justificacion;
    private List<EstadoSolicitud> estados;
    private Long idHecho;


}
