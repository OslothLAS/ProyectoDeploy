package ar.utn.frba.ddsi.dtos;

import ar.utn.frba.ddsi.models.entities.Estadistica;
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

    public static StatDTO fromEntity(Estadistica estadistica) {
        StatDTO dto = new StatDTO();
        dto.setTituloColeccion(estadistica.getTituloColeccion());
        dto.setDescripcion(estadistica.getDescripcion());
        dto.setCantidad(estadistica.getCantidad());
        return dto;
    }


}

