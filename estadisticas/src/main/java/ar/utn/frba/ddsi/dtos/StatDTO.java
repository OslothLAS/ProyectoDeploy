package ar.utn.frba.ddsi.dtos;

import ar.utn.frba.ddsi.models.entities.DescripcionStat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatDTO {
    private String tituloColeccion;
    private String provincia;
    private String categoria;
    private Integer hora;
    private DescripcionStat descripcion;
    private Long cantidad;
    private LocalDateTime fechaStat;
}

