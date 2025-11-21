package ar.utn.frba.ddsi.agregador.models.entities.usuarios;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "registrado", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Contribuyente.class, name = "true"),
        @JsonSubTypes.Type(value = Visualizador.class, name = "false")
})
public interface IUsuario {
        String getNombre();
        Boolean getRegistrado();
        Long getId();
        void setId(Long id);
}

