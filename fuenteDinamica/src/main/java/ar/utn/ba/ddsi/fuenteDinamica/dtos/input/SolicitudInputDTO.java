package ar.utn.ba.ddsi.fuenteDinamica.dtos.input;

import entities.usuarios.Contribuyente;
import lombok.Getter;

@Getter
public class SolicitudInputDTO {

    private Long idHecho;
    //private Long idContribuyente;
    private String justificacion;

    //este no va
    private Contribuyente solicitante;
}
