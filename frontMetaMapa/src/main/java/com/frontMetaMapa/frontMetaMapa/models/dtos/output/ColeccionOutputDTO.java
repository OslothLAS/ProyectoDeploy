package com.frontMetaMapa.frontMetaMapa.models.dtos.output;



import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ColeccionOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private List<FuenteDTO> importadores;
    private List<CriterioDePertenenciaDTO> criteriosDePertenencia;
    private String handle;
    private LocalDateTime fechaYHoraDeActualizacion;
    private String consenso;
}
