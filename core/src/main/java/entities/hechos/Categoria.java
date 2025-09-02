package entities.hechos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categoria")
@Getter
@Setter
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria")
    private String categoria;

    public Categoria(String categoria) {
        this.categoria = categoria;
    }
}
