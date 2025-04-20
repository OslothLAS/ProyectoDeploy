package entities.hechos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
@Builder
@AllArgsConstructor
@Getter
public class DatosHechos {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaHecho;
    private LocalDate fechaCarga;
    private Origen origen;
}
