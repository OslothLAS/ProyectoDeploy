package ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Categoria;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static utils.NormalizadorTexto.normalizarTexto;

@Setter
@Getter
@Entity
@Table(name = "criterio_categoria")
@DiscriminatorValue("categoria")
@NoArgsConstructor
public class CriterioPorCategoria extends CriterioDePertenencia {
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @JsonCreator
    public CriterioPorCategoria(@JsonProperty("categoria") String categoria) {
        this.categoria = new Categoria(categoria);
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return normalizarTexto(hecho.getDatosHechos().getCategoria().getCategoria()).contains(normalizarTexto(this.categoria.getCategoria()));
    }

    @Override
    public MultiValueMap<String, String> aQueryParam(){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("categoria", this.categoria.getCategoria()); // clave: nombre del criterio, valor: su valor
        return map;
    }
}
