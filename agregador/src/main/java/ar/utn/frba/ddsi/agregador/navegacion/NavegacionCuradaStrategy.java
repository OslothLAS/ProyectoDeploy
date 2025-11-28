package ar.utn.frba.ddsi.agregador.navegacion;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component("CURADO")
public class NavegacionCuradaStrategy implements NavegacionStrategy {
    @Override
    public List<Hecho> navegar(List<Hecho> hechos) {
        return hechos.stream()
                .filter(hecho -> hecho.getEsConsensuado() != null && hecho.getEsConsensuado())
                .filter(Hecho::getEsValido)
                .collect(Collectors.toList());
    }
}