package ar.utn.frba.ddsi.agregador.dtos.output;



import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SolicitudOutputDTO {

    private Long id;
    private String username;
    private LocalDateTime fechaDeCreacion;
    private LocalDateTime fechaDeEvaluacion;
    private String justificacion;
    private List<EstadoSolicitudDTO> estados;
    private Long idHecho;


}
