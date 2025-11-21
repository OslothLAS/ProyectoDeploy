package ar.utn.ba.ddsi.fuenteProxy.models.entities.criteriosDePertenencia;

import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Hecho;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import static ar.utn.ba.ddsi.fuenteProxy.utils.NormalizadorTexto.normalizarTexto;

@Setter
@Getter
@NoArgsConstructor
public class CriterioPorCategoria extends CriterioDePertenencia {

    private Categoria categoria;

    @JsonCreator
    public CriterioPorCategoria(@JsonProperty("categoria") String categoria) {
        this.categoria = new Categoria(categoria);
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return normalizarTexto(hecho.getCategoria().getCategoria()).contains(normalizarTexto(this.categoria.getCategoria()));
    }

    @Override
    public MultiValueMap<String, String> aQueryParam(){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("categoria", this.categoria.getCategoria()); // clave: nombre del criterio, valor: su valor
        return map;
    }
}
