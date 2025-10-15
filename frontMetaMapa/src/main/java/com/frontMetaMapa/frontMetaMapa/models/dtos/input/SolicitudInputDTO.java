package com.frontMetaMapa.frontMetaMapa.models.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudInputDTO {
    private Long idHecho;
    private Long idSolicitante;
    private String justificacion;
}