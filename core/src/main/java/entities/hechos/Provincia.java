package entities.hechos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "provincia")
@Getter
@Setter
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    public Provincia(String nombre) {
        this.nombre = nombre;
    }
}
