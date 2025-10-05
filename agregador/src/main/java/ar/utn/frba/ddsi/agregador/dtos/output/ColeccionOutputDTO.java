package ar.utn.frba.ddsi.agregador.dtos.output;

import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.colecciones.Fuente;
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
    private Handle handle;
    private LocalDateTime fechaYHoraDeActualizacion;
    private String consenso;
}
