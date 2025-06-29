package ar.utn.ba.ddsi.fuenteProxy.dtos;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SolicitudDto {
    private Long id;
    private Long id_solicitante;
    private Long id_hecho;
    private String fechaDeCreacion;
    private String fechaDeEvaluacion;
    private String justificacion;
    private String estadoSolicitudEliminacion;
    private List<String> historialDeSolicitud;

    public SolicitudDto() {
    }
}
