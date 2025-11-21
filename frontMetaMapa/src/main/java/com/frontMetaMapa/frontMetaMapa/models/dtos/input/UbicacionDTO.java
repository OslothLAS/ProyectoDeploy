package com.frontMetaMapa.frontMetaMapa.models.dtos.input;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class UbicacionDTO {
    private String latitud;
    private String longitud;

    private LocalidadDTO localidad;
}
