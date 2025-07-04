package ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriterioPorCategoriaDTO implements CriterioPertenenciaDto {
    private String tipo; // debe ser "CriterioPorCategoria"
    private String categoria;

    public String getTipo() {
        return "CriterioPorCategoria";
    }
}
