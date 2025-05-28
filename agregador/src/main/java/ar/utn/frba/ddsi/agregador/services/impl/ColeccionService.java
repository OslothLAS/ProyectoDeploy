package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.CriterioInputDTO;



import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import entities.colecciones.Coleccion;
import entities.colecciones.Fuente;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.criteriosDePertenencia.CriterioPorCategoria;
import entities.criteriosDePertenencia.CriterioPorFecha;
import entities.hechos.Hecho;
import org.springframework.stereotype.Service;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColeccionService implements IColeccionService {

    private final IHechoRepository hechoRepository;

    public ColeccionService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }

//creo la coleccion
    @Override
    public List<Hecho> createColeccion(ColeccionInputDTO coleccionDTO) {
        //lo primero que hago es tomar todas las fuentes importadoras de hechos y a cada una la ionstancio
        //ver la clase fuente en core para entender
        Fuente fuenteEstatica = new Fuente("localhost","8060");
        Fuente fuenteDinamica = new Fuente("localhost","8070");
        Fuente fuenteProxy = new Fuente("localhost","8090");

        /*= coleccionDTO.getImportadores().stream()
                .map(Fuente::new)
                .toList();*/
        List<Fuente> importadores = List.of(fuenteEstatica,fuenteDinamica,fuenteProxy);

        //ahora tomo los criterios y los instancio con la funcion mapearCriterioDTO

        List<CriterioDePertenencia> criterios = coleccionDTO.getCriterios().stream()
                .map(this::mapearCriterioDTO)
                .toList();
    //creo la coleccion
        Coleccion coleccion = new Coleccion(
                coleccionDTO.getTitulo(),
                coleccionDTO.getDescripcion(),
                importadores,
                criterios
        );

        //agarro y tomo todos los hechos de los importadores que tiene mi coleccion

        List<Hecho> todosLosHechos = this.tomarHechosImportadores(importadores);

        //me quedo con los hechos validos
        List<Hecho> hechosValidos = filtrarHechosValidos(todosLosHechos);

        //y ahora me fijo si los hechos cumplen con los criterios de la coleccion y si es asi los meto
        return asignarHechosAColeccion(hechosValidos, coleccion);

        //ahora mi duda es si deberia de guardar estos hechos ya con la coleccion asignada en el repo?
    }

    //aca asigno los hechos a una coleccion
    private List<Hecho> asignarHechosAColeccion(List<Hecho> hechosValidos, Coleccion coleccion) {
        return hechosValidos.stream()
                .filter(coleccion::cumpleCriterios)
                .peek(h -> h.addColeccion(coleccion))
                .toList();
    }


    private CriterioDePertenencia mapearCriterioDTO(CriterioInputDTO dto) {
        return switch (dto.getTipo().toLowerCase()) {
            case "categoria" -> new CriterioPorCategoria(dto.getCategoria());
            case "fecha" -> new CriterioPorFecha(dto.getFechaInicio(), dto.getFechaFin());
            default -> throw new IllegalArgumentException("Tipo de criterio desconocido: " + dto.getTipo());
        };
    }

    private List<Hecho> tomarHechosImportadores(List<Fuente> importadores) {
        return importadores.stream()
                .flatMap(i -> i.obtenerHechos().stream())
                .toList();
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
        Fuente fuenteEstatica = new Fuente("localhost","8060");
        Fuente fuenteDinamica = new Fuente("localhost","8070");
        Fuente fuenteProxy = new Fuente("localhost","8090");
        List<Fuente> importadores = List.of(fuenteEstatica,fuenteDinamica,fuenteProxy);
        List<Hecho> hechos = this.tomarHechosImportadores(importadores);
        List<Hecho> hechosValidos = filtrarHechosValidos(hechos);
        List<Coleccion> colecciones = this.traerColecciones(hechos);
        colecciones.forEach(coleccion -> coleccion.filtrarHechos(hechosValidos));
        hechos.forEach(hechoRepository::save);
    }

    //Crea una lista con todas las colecciones existentes para usarse (por el momento)
    //en actualizarHechos
    public List<Coleccion> traerColecciones(List<Hecho> hechos){
        Set<Coleccion> colecciones = new HashSet<>();
        hechos.forEach(hecho -> colecciones.addAll(hecho.getColecciones()));
        return new ArrayList<>(colecciones);
    }

    //Esto es solo para agregar los hechos validos
    private List<Hecho> filtrarHechosValidos(List<Hecho> hechos){
       return hechos.stream().filter(Hecho::getEsValido).collect(Collectors.toList());
    }



}
