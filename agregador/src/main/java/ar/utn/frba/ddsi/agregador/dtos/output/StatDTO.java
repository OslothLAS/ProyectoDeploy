package ar.utn.frba.ddsi.agregador.dtos.output;

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
//      descripcion
//hechos_provincia_coleccion
//hechos_categoria
//hechos_categoria_provincia
//hechos_categoria_hora
//solicitudes_spam

