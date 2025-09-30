package ar.utn.ba.ddsi.fuenteEstatica.entities.criteriosDePertenencia;

import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.Hecho;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "tipo",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriterioPorCategoria.class, name = "categoria")
})
@Getter
public abstract class CriterioDePertenencia {
    public boolean cumpleCriterio(Hecho hecho){
        return true;
    }

    public MultiValueMap<String, String> aQueryParam(){
        return new LinkedMultiValueMap<>();
    }
}
