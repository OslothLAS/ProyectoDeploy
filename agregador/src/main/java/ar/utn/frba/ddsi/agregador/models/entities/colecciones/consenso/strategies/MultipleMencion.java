package ar.utn.frba.ddsi.agregador.models.entities.colecciones.consenso.strategies;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("MULTIPLE_MENCION")
@Getter
public class MultipleMencion implements IAlgoritmoConsenso {
    public final String nombre = "multiple_mencion";

    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
        List<Hecho> filtradosPorTitulo =  filtrarHechosConDescripcionDiferente(hechos);
        return FiltroConsensuados.obtenerHechos(fuentes, hechos, 2);

    }

    private List<Hecho> filtrarHechosConDescripcionDiferente(List<Hecho> hechos) {
        if (hechos == null) {
            return Collections.emptyList();
        }

        Map<String, List<Hecho>> hechosPorTitulo = hechos.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Hecho::getTitulo));


        return hechosPorTitulo.values().stream()
                .filter(lista -> lista.stream()
                        .map(Hecho::getDescripcion)
                        .filter(Objects::nonNull)
                        .distinct()
                        .count() > 1
                )
                .flatMap(List::stream) // aplanamos para devolver la lista de hechos originales
                .collect(Collectors.toList());
    }
}
