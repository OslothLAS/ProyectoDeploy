package ar.utn.frba.ddsi.agregador.dtos.input;

import entities.criteriosDePertenencia.CriterioDePertenencia;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ColeccionInputDTO {
    private String titulo;
    private String descripcion;
    private List<String> importadores;
    private List<CriterioDePertenencia> criterios;
}
