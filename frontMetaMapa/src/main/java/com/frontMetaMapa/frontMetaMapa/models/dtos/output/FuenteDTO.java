package com.frontMetaMapa.frontMetaMapa.models.dtos.output;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class FuenteDTO {
    private Long id;
    private String ip;
    private String puerto;
    private FuenteOrigen nombre;
}
