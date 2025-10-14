package com.frontMetaMapa.frontMetaMapa.models.DTOS.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class HechoOutputDTO {
    private Long id;
    private String username;
    private Boolean esValido;
    private List<String> multimedia;
    private List<String> etiquetas = new ArrayList<>();
    private List<ColeccionOutputDTO> colecciones = new ArrayList<>();
    private List<String> handles = new ArrayList<>();
    private Origen origen;
    private FuenteOrigen fuenteOrigen;
    private Boolean mostrarDatos; //ver esto
    private LocalDateTime fechaCreacion;
    private Duration plazoEdicion;
    private Boolean esEditable;

    //datos
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaHecho;
}
