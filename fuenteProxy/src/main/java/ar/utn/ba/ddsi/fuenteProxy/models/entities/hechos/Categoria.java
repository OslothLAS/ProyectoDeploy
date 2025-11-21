package ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static ar.utn.ba.ddsi.fuenteProxy.utils.NormalizadorTexto.normalizarTrimTexto;

@NoArgsConstructor

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categoria {
    private String categoria;
    private String categoriaNormalizada;

    @JsonCreator
    public Categoria(@JsonProperty("categoria") String categoria) {
        this.categoria = categoria;
        this.categoriaNormalizada = normalizarTrimTexto(categoria);
    }
}
