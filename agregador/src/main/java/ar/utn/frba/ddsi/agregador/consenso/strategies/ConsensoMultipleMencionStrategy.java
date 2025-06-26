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

public class ConsensoMultipleMencionStrategy extends ConsensoStrategy{

    @Override
    public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes, List<Hecho> hechos) {
        return super.obtenerHechos(fuentes, hechos, 2);
    }


}
