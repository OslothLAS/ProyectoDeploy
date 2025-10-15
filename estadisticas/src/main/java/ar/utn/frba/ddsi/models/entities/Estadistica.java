package ar.utn.frba.ddsi.models.entities;

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

    @Column(name = "titulo_coleccion")
    private String tituloColeccion;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "hora")
    private Integer hora;  // hora del día (0-23)

    @Enumerated(EnumType.STRING)
    @Column(name = "descripcion")
    private DescripcionStat descripcion;

    @Column(name = "cantidad")
    private Long cantidad;

    @Column(name = "fecha_stat")
    private LocalDateTime fechaStat;
}