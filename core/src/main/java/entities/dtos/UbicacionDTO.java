package entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UbicacionDTO {
    private String latitud;
    private String longitud;

    private LocalidadDTO localidad;
}
