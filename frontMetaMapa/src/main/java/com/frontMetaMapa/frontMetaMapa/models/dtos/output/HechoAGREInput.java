package com.frontMetaMapa.frontMetaMapa.models.dtos.output;


import com.frontMetaMapa.frontMetaMapa.models.dtos.input.MultimediaDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HechoAGREInput {
    private Long id;

    private String username;
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaHecho;
    private List<MultimediaDTO> multimedia;

    private Origen origen;
    private Boolean mostrarDatos; //ver esto
    private Boolean esValido;
    private LocalDateTime fechaCreacion;


    private List<String> etiquetas = new ArrayList<>();
    private List<String> handles = new ArrayList<>();
    private FuenteOrigen fuenteOrigen;
    private Duration plazoEdicion;
    private Boolean esEditable;
}

