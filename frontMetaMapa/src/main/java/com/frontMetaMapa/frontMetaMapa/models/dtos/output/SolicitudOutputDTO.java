package com.frontMetaMapa.frontMetaMapa.models.dtos.output;
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
    private String evaluador;
    private String Estado;
    private Long IdHecho;
}
