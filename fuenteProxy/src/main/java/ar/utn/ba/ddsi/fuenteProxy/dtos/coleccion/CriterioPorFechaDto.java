package ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriterioPorFechaDto implements CriterioPertenenciaDto {
    private String tipo; // debe ser "CriterioPorFecha"
    private String fechaDesde;
    private String fechaHasta;
    @Override
    public String getTipo() {
        return "CriterioPorFecha";
    }
}
