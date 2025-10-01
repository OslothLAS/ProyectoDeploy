package ar.utn.ba.ddsi.fuenteDinamica.dtos.input;

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

    private List<MultimediaDTO> multimedia;
    //private Long id; //esto lo vemos despues (id del usuario)
    private Boolean mostrarDatos;
}
