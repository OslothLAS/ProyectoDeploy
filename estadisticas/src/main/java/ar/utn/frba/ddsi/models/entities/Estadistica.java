package ar.utn.frba.ddsi.models.entities;

import ar.utn.frba.ddsi.dtos.StatDTO;
import entities.colecciones.Coleccion;
import entities.hechos.Categoria;
import entities.hechos.Provincia;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "estadistica")
public class Estadistica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tituloColeccion")
    private String tituloColeccion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "cantidad")
    private Long cantidad;

    // Método estático para convertir desde DTO
    public static Estadistica fromDTO(StatDTO dto) {
        Estadistica estadistica = new Estadistica();
        estadistica.setTituloColeccion(dto.getTituloColeccion());
        estadistica.setDescripcion(dto.getDescripcion());
        estadistica.setCantidad(dto.getCantidad());
        return estadistica;
    }
}