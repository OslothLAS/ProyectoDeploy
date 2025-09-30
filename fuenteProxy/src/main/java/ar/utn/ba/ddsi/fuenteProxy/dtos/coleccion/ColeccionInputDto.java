package ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ColeccionInputDto {
    private String titulo;

    private String descripcion;

    private List<CriterioPertenenciaDto> criteriosDePertenencia;

}