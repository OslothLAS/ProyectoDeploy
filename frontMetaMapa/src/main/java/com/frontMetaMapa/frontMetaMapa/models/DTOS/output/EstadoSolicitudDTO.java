package com.frontMetaMapa.frontMetaMapa.models.DTOS.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoSolicitudDTO {
    private Long id;
    private UsuarioDTO evaluador;
    private PosibleEstadoSolicitud estado;
    private LocalDateTime fechaDeCambio;
    private LocalDateTime fechaDeCreacion;
    private Boolean spam;
}
