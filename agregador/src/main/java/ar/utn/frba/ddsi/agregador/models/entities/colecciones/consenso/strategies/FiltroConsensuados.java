package ar.utn.frba.ddsi.agregador.models.entities.colecciones.consenso.strategies;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.FuenteOrigen;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;

import java.util.*;
import java.util.stream.Collectors;

public class FiltroConsensuados {

    public static List<Hecho> obtenerHechos(List<Fuente> fuentes, List<Hecho> hechos, int cantidadDeFuentesQueCoinciden) {

        Set<FuenteOrigen> origenesDeFuentes = fuentes.stream()
                .filter(Objects::nonNull)
                .map(Fuente::getOrigenHechos)
                .collect(Collectors.toSet());

        // agrupo los hechos iguales por DatosHechos (uso como ID los DatosHechos)
        Map<List<String>, List<Hecho>> hechosPorDatos = hechos.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(h -> List.of(h.getTitulo(), h.getDescripcion())));

        // filtro los hechos presentes en múltiples orígenes
        List<Hecho> hechosFiltrados = new ArrayList<>();

        hechosPorDatos.forEach((datos, listaHechos) -> {

            // cuento orígenes distintos para estos DatosHechos
            long countOrigenesDistintos = listaHechos.stream()
                    .map(Hecho::getFuenteOrigen)
                    .distinct()
                    .filter(origenesDeFuentes::contains)
                    .count();

            if (countOrigenesDistintos >= cantidadDeFuentesQueCoinciden) {
                // añado el primer hecho del grupo (o podrías añadir todos)
                hechosFiltrados.addAll(listaHechos);
            }
        });

        return hechosFiltrados;
    }

}
