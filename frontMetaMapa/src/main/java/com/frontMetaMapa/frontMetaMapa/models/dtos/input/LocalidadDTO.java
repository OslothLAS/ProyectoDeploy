package com.frontMetaMapa.frontMetaMapa.models.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class LocalidadDTO {
    private String nombre;
    private ProvinciaDTO provincia;
}
