package com.frontMetaMapa.frontMetaMapa.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuenteInputDTO {
  private Long id;
  private String url;
  private FuenteOrigen origen;
  private String origenS;
}
