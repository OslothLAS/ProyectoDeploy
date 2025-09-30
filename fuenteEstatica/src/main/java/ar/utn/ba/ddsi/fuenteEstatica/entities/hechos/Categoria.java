package ar.utn.ba.ddsi.fuenteEstatica.entities.hechos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static utils.NormalizadorTexto.normalizarTrimTexto;

@NoArgsConstructor
@Getter
@Setter
public class Categoria {

    private String categoria;
    private String categoriaNormalizada;

    @JsonCreator
    public Categoria(@JsonProperty("categoria") String categoria) {
        this.categoria = categoria;
        this.categoriaNormalizada = normalizarTrimTexto(categoria);
    }
}
