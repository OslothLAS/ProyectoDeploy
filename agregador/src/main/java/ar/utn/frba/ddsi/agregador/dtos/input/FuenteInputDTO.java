package ar.utn.frba.ddsi.agregador.dtos.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuenteInputDTO {
  private Long id;
  private String ip;
  private String puerto;
}
