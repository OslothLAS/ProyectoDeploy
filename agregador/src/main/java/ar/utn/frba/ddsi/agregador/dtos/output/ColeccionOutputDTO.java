package ar.utn.frba.ddsi.agregador.dtos.output;


import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
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
