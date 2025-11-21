package com.frontMetaMapa.frontMetaMapa.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class HechoApiInputDto {
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaHecho;
    private boolean mostrarDatos;
    private List<MultimediaDTO> multimedia;

}
