package com.frontMetaMapa.frontMetaMapa.models.DTOS.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.CriterioDePertenenciaDTO;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.FuenteDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ColeccionInputDTO {
    private String id;
    private String titulo;
    private String descripcion;
    @JsonProperty("fuentes")
    private List<FuenteDTO> fuentes;
    @JsonProperty("criterios")
    private List<CriterioDePertenenciaDTO> criterios;
    private String estrategiaConsenso;
}
