package entities.criteriosDePertenencia;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import entities.hechos.Hecho;
import org.springframework.util.MultiValueMap;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "tipo",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriterioPorCategoria.class, name = "categoria")
})
public interface CriterioDePertenencia {
    boolean cumpleCriterio(Hecho hecho);

    MultiValueMap<String, String> aQueryParam();
}
