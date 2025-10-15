package ar.utn.frba.ddsi.agregador.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CriterioDePertenenciaDTO {
    private Long id;
    private String tipo;
    private String valor;
}
