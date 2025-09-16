package entities.colecciones.consenso.strategies;

import entities.hechos.FuenteOrigen;
import entities.hechos.Hecho;
import entities.colecciones.Fuente;

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
                .collect(Collectors.groupingBy(Hecho::getTituloYDescripcion));

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
                hechosFiltrados.add(listaHechos.get(0));
            }
        });

        return hechosFiltrados;
    }




}
