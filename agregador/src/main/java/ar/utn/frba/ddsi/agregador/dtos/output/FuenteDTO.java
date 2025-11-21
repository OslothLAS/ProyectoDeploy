package ar.utn.frba.ddsi.agregador.dtos.output;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.FuenteOrigen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class FuenteDTO {
    private Long id;
    private String ip;
    private String puerto;
    private FuenteOrigen nombre;
}
