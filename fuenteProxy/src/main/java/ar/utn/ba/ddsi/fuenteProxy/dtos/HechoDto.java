package ar.utn.ba.ddsi.fuenteProxy.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HechoDto {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fecha_hecho;
    private String created_at;
    private String updated_at;
}
