package entities.colecciones.consenso.strategies;

import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.colecciones.Fuente;
import entities.hechos.Origen;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ConsensoStrategy {

    public abstract List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos);


    public List<Hecho> obtenerHechos(List<Fuente> fuentes, List<Hecho> hechos, int cantidadDeFuentesQueCoinciden) {

        // mapeo orígenes de las fuentes disponibles
        Set<Origen> origenesDeFuentes = fuentes.stream()
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
                    .map(Hecho::getOrigen)
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
