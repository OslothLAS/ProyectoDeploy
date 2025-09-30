package ar.utn.frba.ddsi.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estadistica")
public class StatDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tituloColeccion")
    private String tituloColeccion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "cantidad")
    private Long cantidad;
}

