package entities.hechos;

import lombok.*;

import java.time.LocalDate;
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DatosHechos {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaHecho;
}
