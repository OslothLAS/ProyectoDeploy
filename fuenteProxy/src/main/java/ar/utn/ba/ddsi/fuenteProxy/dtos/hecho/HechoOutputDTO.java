package ar.utn.ba.ddsi.fuenteProxy.dtos.hecho;

import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.FuenteOrigen;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Handle;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Origen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HechoOutputDTO {
    private Boolean esValido;
    private List<MultimediaDTO> multimedia;
    private Origen origen;
    private FuenteOrigen fuenteOrigen;
    private LocalDateTime fechaCreacion;
    private List<Handle> handles = new ArrayList<>();
    private Boolean esConsensuado;

    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaHecho;
}
