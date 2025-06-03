package ar.utn.frba.ddsi.agregador.dtos.output;

import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.colecciones.Fuente;
import lombok.Setter;

import java.util.List;

@Setter
public class ColeccionOutputDTO {
    String titulo;
    String descripcion;
    List<Fuente> importadores;
    List<CriterioDePertenencia> criteriosDePertenencia;

    public ColeccionOutputDTO(String titulo, String descripcion, List<Fuente> importadores, List<CriterioDePertenencia> criteriosDePertenencia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.importadores = importadores;
        this.criteriosDePertenencia = criteriosDePertenencia;
    }
}
