package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import org.springframework.stereotype.Service;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColeccionService implements IColeccionService {

    private IHechoRepository hechoRepository;

    public ColeccionService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }


    @Override
    public List<Hecho> createColeccion(Coleccion coleccion) {
        // Obtener todos los hechos de todos los importadores
        List<Hecho> todosLosHechos = coleccion.getImportadores().stream()
                .flatMap(importador -> importador.obtenerHechos().stream())
                .toList();

        // Filtrar según los criterios y agregar la colección a cada hecho filtrado
        List<Hecho> hechosFiltrados = todosLosHechos.stream()
                .filter(coleccion::cumpleCriterios)
                .peek(hecho -> hecho.addColeccion(coleccion)) // ← aquí se agrega la colección
                .toList();

        return hechosFiltrados;
    }

    @Override
    public List<Hecho> getColeccion(String idColeccion) {
        List<Hecho> todosLosHechos = filtrarHechosValidos(hechoRepository.findAll());

        return todosLosHechos.stream()
                .filter(hecho -> hecho.getColecciones().stream()
                        .anyMatch(coleccion -> coleccion.getHandle().getValue().equals(idColeccion)))
                .collect(Collectors.toList());
    }

    public void actualizarHechos(){
        List<Hecho> hechos = filtrarHechosValidos(hechoRepository.findAll());
        List<Coleccion> colecciones = this.traerColecciones();
        colecciones.forEach(coleccion -> coleccion.filtrarHechos(hechos));
        hechos.forEach(hecho -> hechoRepository.save(hecho));
    }

    //Crea una lista con todas las colecciones existentes para usarse (por el momento)
    //en actualizarHechos
    public List<Coleccion> traerColecciones(){
        Set<Coleccion> colecciones = new HashSet<>();
        List<Hecho> hechos = hechoRepository.findAll();
        hechos.forEach(hecho -> colecciones.addAll(hecho.getColecciones()));
        return new ArrayList<>(colecciones);
    }

    //Esto es solo para agregar los hechos validos
    private List<Hecho> filtrarHechosValidos(List<Hecho> hechos){
       return hechos.stream().filter(Hecho::getEsValido).collect(Collectors.toList());
    }



}
