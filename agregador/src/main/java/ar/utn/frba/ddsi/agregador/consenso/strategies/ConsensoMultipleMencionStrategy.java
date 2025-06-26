package ar.utn.frba.ddsi.agregador.consenso.strategies;

import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionMemoryRepository;
import entities.colecciones.Coleccion;
import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.colecciones.Fuente;
import entities.hechos.Origen;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ConsensoMultipleMencionStrategy implements ConsensoStrategy{

    private IColeccionMemoryRepository colecciones;
    /*múltiples menciones: si al menos dos fuentes contienen un mismo hecho y
     ninguna otra fuente contiene otro de igual título pero diferentes atributos
     se lo considera consensuado;*/


    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {

        // mapeo orígenes de las fuentes disponibles
        Set<Origen> origenesDeFuentes = fuentes.stream()
                .filter(Objects::nonNull)
                .map(Fuente::getOrigenHechos)
                .collect(Collectors.toSet());

        // agrupo los hechos iguales por DatosHechos (uso como ID los DatosHechos)
        Map<DatosHechos, List<Hecho>> hechosPorDatos = hechos.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Hecho::getDatosHechos));

        // filtro los hechos presentes en múltiples orígenes
        List<Hecho> hechosFiltrados = new ArrayList<>();

        hechosPorDatos.forEach((datos, listaHechos) -> {

            // cuento orígenes distintos para estos DatosHechos
            long countOrigenesDistintos = listaHechos.stream()
                    .map(Hecho::getOrigen)
                    .distinct()
                    .filter(origenesDeFuentes::contains)
                    .count();

            if (countOrigenesDistintos >= 2) {
                // añado el primer hecho del grupo (o podrías añadir todos)
                hechosFiltrados.add(listaHechos.get(0));
            }
        });

        return hechosFiltrados;
    }
}
