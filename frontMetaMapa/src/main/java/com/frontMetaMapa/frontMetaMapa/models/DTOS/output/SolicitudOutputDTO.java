package com.frontMetaMapa.frontMetaMapa.models.DTOS.output;


import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.EstadoSolicitudDTO;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.UsuarioDTO;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SolicitudOutputDTO {

    private Long id;
    private UsuarioDTO solicitante;
    private LocalDateTime fechaDeCreacion;
    private LocalDateTime fechaDeEvaluacion;
    private String justificacion;
    private List<EstadoSolicitudDTO> estados;
    private Long idHecho;


}
