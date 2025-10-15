package com.frontMetaMapa.frontMetaMapa.models.dtos.input;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ColeccionInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    @JsonProperty("fuentes")
    private List<FuenteInputDTO> fuentes;
    @JsonProperty("criterios")
    private List<CriterioDePertenenciaDTO> criterios;
    private String estrategiaConsenso;
}
