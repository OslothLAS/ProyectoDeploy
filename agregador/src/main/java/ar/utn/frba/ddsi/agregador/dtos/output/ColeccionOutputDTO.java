package ar.utn.frba.ddsi.agregador.dtos.output;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Handle;
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
    private Handle handle;
    private LocalDateTime fechaYHoraDeActualizacion;
    private String consenso;
}
