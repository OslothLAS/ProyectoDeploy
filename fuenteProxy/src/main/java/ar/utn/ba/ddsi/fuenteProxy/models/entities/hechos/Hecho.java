package ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hecho {
    private Long id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHecho;
    private List<Handle> handles = new ArrayList<>();

    private List<Multimedia> multimedia;
    private Origen origen;
    private FuenteOrigen fuenteOrigen;
    private LocalDateTime fechaCreacion;
    private Boolean esValido;
    private Boolean esConsensuado;
}