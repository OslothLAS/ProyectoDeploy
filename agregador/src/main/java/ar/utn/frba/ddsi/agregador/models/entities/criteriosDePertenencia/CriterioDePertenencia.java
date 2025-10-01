package ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "rol",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriterioPorCategoria.class, name = "categoria")
})
@Entity
@Table(name = "criterio_pertenencia")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "rol", discriminatorType = DiscriminatorType.STRING)
@Getter
public abstract class CriterioDePertenencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public boolean cumpleCriterio(Hecho hecho){
        return true;
    }

    public MultiValueMap<String, String> aQueryParam(){
        return new LinkedMultiValueMap<>();
    }
}
