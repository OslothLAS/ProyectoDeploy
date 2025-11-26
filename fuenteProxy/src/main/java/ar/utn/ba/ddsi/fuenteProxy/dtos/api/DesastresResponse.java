package ar.utn.ba.ddsi.fuenteProxy.dtos.api;

import ar.utn.ba.ddsi.fuenteProxy.dtos.hecho.HechoInputDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DesastresResponse {
    private List<HechoInputDTO> data;
}
