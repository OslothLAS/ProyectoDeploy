package com.frontMetaMapa.frontMetaMapa.models.dtos.Api;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SolicitudApiOutputDto {
    private Long id;
    private String username;
    private LocalDateTime fechaDeCreacion;
    private LocalDateTime fechaDeEvaluacion;
    private String justificacion;
    private List<EstadoSolicitud> estados;
    private Long idHecho;
}
