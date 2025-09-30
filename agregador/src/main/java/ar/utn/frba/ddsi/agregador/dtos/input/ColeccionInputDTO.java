package ar.utn.frba.ddsi.agregador.dtos.input;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ColeccionInputDTO {
    private String titulo;
    private String descripcion;
    @JsonProperty("fuentes")
    private List<Fuente> fuentes;
    @JsonProperty("criterios")
    private List<CriterioDePertenencia> criterios;
    private String estrategiaConsenso;
}
