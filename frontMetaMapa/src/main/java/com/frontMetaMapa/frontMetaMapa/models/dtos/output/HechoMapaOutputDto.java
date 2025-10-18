package com.frontMetaMapa.frontMetaMapa.models.dtos.output;

import lombok.Data;

@Data
public class HechoMapaOutputDto {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
}
