package ar.utn.frba.ddsi.agregador.dtos.output;

import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.colecciones.Fuente;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
public class ColeccionOutputDTO {
    String titulo;
    String descripcion;
    List<Fuente> importadores;
    List<CriterioDePertenencia> criteriosDePertenencia;
}
