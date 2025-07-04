package entities.criteriosDePertenencia;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import entities.hechos.Hecho;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import static utils.NormalizadorTexto.normalizarTexto;

@Getter
public class CriterioPorCategoria implements CriterioDePertenencia{
    private final String categoria;

    @JsonCreator
    public CriterioPorCategoria(@JsonProperty("categoria") String categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return normalizarTexto(hecho.getDatosHechos().getCategoria()).contains(normalizarTexto(categoria));
    }

    @Override
    public MultiValueMap<String, String> aQueryParam(){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("categoria", this.categoria); // clave: nombre del criterio, valor: su valor
        return map;
    }
}
