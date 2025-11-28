package ar.utn.ba.ddsi.fuenteDinamica.dtos.output;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.UbicacionDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.FuenteOrigen;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Multimedia;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Origen;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
public class HechoOutputDTO {
    private Long id;
    private String username;
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaHecho;
    private List<Multimedia> multimedia;

    private FuenteOrigen fuenteOrigen;
    private Origen origen;
    private Boolean mostrarDatos;
    private Boolean esValido;
    private LocalDateTime fechaCreacion;
}
