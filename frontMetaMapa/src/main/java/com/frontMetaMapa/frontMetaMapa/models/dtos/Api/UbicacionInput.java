package com.frontMetaMapa.frontMetaMapa.models.dtos.Api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionInput {
    private Double latitud;
    private Double longitud;
    private  Localidad localidad;
}
