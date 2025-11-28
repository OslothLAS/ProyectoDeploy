package com.frontMetaMapa.frontMetaMapa.models.dtos.output;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.FuenteOrigen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class FuenteDTO {
    private Long id;
    private String url;
    private FuenteOrigen nombre;
}

