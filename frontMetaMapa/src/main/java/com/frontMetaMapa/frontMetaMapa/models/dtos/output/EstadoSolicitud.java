package com.frontMetaMapa.frontMetaMapa.models.dtos.output;

import java.time.LocalDateTime;

public class EstadoSolicitud {
    private Long id;
    private UsuarioDTO evaluador;
    private PosibleEstadoSolicitud estado;
    private LocalDateTime fechaDeCambio;
    private LocalDateTime fechaDeCreacion;
    private Boolean spam;
}
