package ar.utn.ba.ddsi.fuenteDinamica.dtos.input;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Multimedia;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Origen;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class HechoDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaHecho;
    private List<Multimedia> multimedia;

    private Origen origen;
    private Boolean mostrarDatos;
}
