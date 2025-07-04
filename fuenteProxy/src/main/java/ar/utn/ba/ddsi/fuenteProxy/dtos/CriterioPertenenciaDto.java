package ar.utn.ba.ddsi.fuenteProxy.dtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriterioPorCategoriaDTO.class, name = "CriterioPorCategoria"),
        @JsonSubTypes.Type(value = CriterioPorFechaDto.class, name = "CriterioPorFecha")
})

public interface CriterioPertenenciaDto {
    String getTipo();
}
