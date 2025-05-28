package ar.utn.frba.ddsi.agregador.dtos.input;

import lombok.Getter;

import java.util.List;
@Getter
public class ColeccionInputDTO {
    private String titulo;
    private String descripcion;
    private List<String> importadores; // simplified view of importadores
    private List<CriterioInputDTO> criterios;      // could be strings, IDs, or DTOs
}
