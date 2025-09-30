package ar.utn.ba.ddsi.fuenteEstatica.entities.hechos;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaHecho;

    private List<Multimedia> multimedia;
    private Origen origen;
    private FuenteOrigen fuenteOrigen;
    private LocalDateTime fechaCreacion;
    private Boolean esValido;
}