package ar.utn.ba.ddsi.fuenteProxy.dtos.hecho;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.colecciones.Handle;
import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class HechoDto {

    // Getters
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private Boolean esValido;
    private Map<Handle,Boolean> colecciones = new HashMap<>();

    @JsonProperty("fecha_hecho")
    private String fechaHecho;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

}
