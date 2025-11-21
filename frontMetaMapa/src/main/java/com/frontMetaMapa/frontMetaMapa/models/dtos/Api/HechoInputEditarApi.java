package com.frontMetaMapa.frontMetaMapa.models.dtos.Api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HechoInputEditarApi {
    private String titulo;
    private String descripcion;
    private Boolean mostrarDatos;
    private UbicacionInput ubicacion;
    //private List<Multimedia> multimedia;
}