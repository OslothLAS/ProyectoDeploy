package ar.utn.frba.ddsi.agregador.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudInputDTO {
    private Long idHecho;
    private String username;
    private String justificacion;
}