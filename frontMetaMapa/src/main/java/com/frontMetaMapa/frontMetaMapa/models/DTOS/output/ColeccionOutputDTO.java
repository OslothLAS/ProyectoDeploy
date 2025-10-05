package com.frontMetaMapa.frontMetaMapa.models.DTOS.output;

import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.FuenteDTO;
import lombok.Data;
import lombok.Setter;

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