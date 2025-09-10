package ar.utn.frba.ddsi.agregador.dtos.output;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatDTO {

    private String tituloColeccion;


    private String descripcion;

    private Long cantidad;
}
