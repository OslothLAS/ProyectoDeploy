package ar.utn.ba.ddsi.fuenteProxy.dtos.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LocalidadDTO {
    private String nombre;
    private ProvinciaDTO provincia;
}
