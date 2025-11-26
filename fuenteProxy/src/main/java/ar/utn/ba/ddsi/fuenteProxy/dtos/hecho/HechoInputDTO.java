package ar.utn.ba.ddsi.fuenteProxy.dtos.hecho;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HechoInputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private LocalDateTime fecha_hecho;
    private LocalDateTime fecha_creacion;
}
