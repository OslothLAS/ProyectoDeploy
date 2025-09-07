package entities.criteriosDePertenencia;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import entities.hechos.Categoria;
import entities.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import static utils.NormalizadorTexto.normalizarTexto;

@Getter
@Entity
@Table(name = "criterio_categoria")
@DiscriminatorValue("categoria")
public class CriterioPorCategoria extends CriterioDePertenencia{
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoria_id")
    private final Categoria categoria;

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
