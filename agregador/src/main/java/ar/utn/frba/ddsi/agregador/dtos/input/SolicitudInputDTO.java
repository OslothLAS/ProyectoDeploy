package ar.utn.frba.ddsi.agregador.dtos.input;

import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Contribuyente;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SolicitudInputDTO {
    private String titulo;
    private String descripcion;
    private Contribuyente solicitante;
    private String justificacion;
}