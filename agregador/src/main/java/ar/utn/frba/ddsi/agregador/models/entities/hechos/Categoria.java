package ar.utn.frba.ddsi.agregador.models.entities.hechos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static utils.NormalizadorTexto.normalizarTrimTexto;

@NoArgsConstructor

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria")
        private String categoria;

    @Column(name = "categoria_normalizada", unique = true)
    @EqualsAndHashCode.Include
    private String categoriaNormalizada;

    @JsonCreator
    public Categoria(@JsonProperty("categoria") String categoria) {
        this.categoria = categoria;
        this.categoriaNormalizada = normalizarTrimTexto(categoria);
    }
}
