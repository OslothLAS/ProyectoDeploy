package ar.utn.ba.ddsi.fuenteEstatica.entities.dtos;

import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.FuenteOrigen;
import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.Origen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
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

    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaHecho;
}
