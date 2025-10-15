package com.frontMetaMapa.frontMetaMapa.models.dtos.output;



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
    private List<EstadoSolicitud> estados;
    private Long idHecho;


}
