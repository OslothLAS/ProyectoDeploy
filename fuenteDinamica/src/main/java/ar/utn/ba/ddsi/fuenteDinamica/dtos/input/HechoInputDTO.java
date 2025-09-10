package ar.utn.ba.ddsi.fuenteDinamica.dtos.input;

import entities.hechos.DatosHechos;
import entities.hechos.Multimedia;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class HechoInputDTO {
    private DatosHechos datosHechos;
    private List<Multimedia> multimedia;
    private Long id; //esto lo vemos despues (id del usuario)
    private Boolean mostrarDatos;
}
