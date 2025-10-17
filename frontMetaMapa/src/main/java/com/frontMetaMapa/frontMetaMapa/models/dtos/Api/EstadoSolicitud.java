package com.frontMetaMapa.frontMetaMapa.models.dtos.Api;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EstadoSolicitud {
    private Long id;
    private String evaluador; // username del evaluador
    private PosibleEstadoSolicitud estado;
    private LocalDateTime fechaDeCambio; // fecha de evaluación
    private LocalDateTime fechaDeCreacion; // cuando se creó el estado
    private Boolean spam;
}
