package ar.utn.ba.ddsi.fuenteDinamica.dtos.input;

import lombok.Getter;

@Getter
public class SolicitudInputDTO {

    private Long idHecho;
    private Long idContribuyente;
    private String justificacion;
}
