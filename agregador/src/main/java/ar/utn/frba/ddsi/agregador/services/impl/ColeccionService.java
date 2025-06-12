package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.CriterioInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.ColeccionOutputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionMemoryRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import entities.Importador;
import entities.colecciones.Coleccion;
import entities.colecciones.Fuente;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.hechos.Hecho;
import entities.hechos.Origen;
import org.springframework.stereotype.Service;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import java.util.*;
import java.util.stream.Collectors;
import static ar.utn.frba.ddsi.agregador.utils.ColeccionUtil.dtoToColeccion;

@Service
public class ColeccionService implements IColeccionService {

    private final IHechoRepository hechoRepository;
    private IColeccionMemoryRepository coleccionMemoryRepository;

    public ColeccionService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }

//creo la coleccion
    @Override
    public List<Hecho> createColeccion(ColeccionInputDTO coleccionDTO) {
        //lo primero que hago es tomar todas las fuentes importadoras de hechos y a cada una la ionstancio
        List<Fuente> importadores = this.instanciarFuentes();
        //ahora tomo los criterios y los instancio con la funcion mapearCriterioDTO
        List<CriterioDePertenencia> criterios = coleccionDTO.getCriterios().stream()
                .map(this::mapearCriterioDTO)
                .toList();
        //creo la coleccion
        Coleccion nuevaColeccion = this.dtoToColeccion(coleccionDTO, importadores);
        //agarro y tomo todos los hechos de los importadores que tiene mi coleccion
        List<Hecho> todosLosHechos = this.tomarHechosImportadores(importadores);


        //me quedo con los hechos validos
        List<Hecho> hechosValidos = filtrarHechosValidos(todosLosHechos);

        //y ahora me fijo si los hechos cumplen con los criterios de la coleccion y si es asi los meto
        List<Hecho> hechos = asignarHechosAColeccion(hechosValidos,nuevaColeccion);
        hechos.forEach(hechoRepository::save);
        return hechos;
    }


    private List<CriterioDePertenencia> obtenerCriteriosDTO (ColeccionInputDTO coleccion){
        return coleccion.getCriterios().stream()
                .map(this::mapearCriterioDTO)
                .toList();
    }


    //aca asigno los hechos a una coleccion
    private List<Hecho> asignarHechosAColeccion(List<Hecho> hechosValidos, Coleccion coleccion) {
        return hechosValidos.stream()
                .filter(coleccion::cumpleCriterios)
                .peek(h -> h.addColeccion(coleccion))
                .toList();
    }


//aca se debería mandar la request para filtrar los hechos en cada fuente
    private List<Hecho> tomarHechosImportadores(List<Fuente> importadores, List<CriterioDePertenencia> criterios) {
        return importadores.stream()
                .flatMap(fuente ->
                        fuente.obtenerHechos(criterios).stream()
                                .peek(hecho -> hecho.setOrigen(fuente.getOrigenHechos()))
                )
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
        List<Hecho> hechos = this.tomarHechosImportadores(this.instanciarFuentes());

        List<Hecho> hechosValidos = filtrarHechosValidos(hechos);
        List<Coleccion> colecciones = this.traerColecciones(hechos);
        colecciones.forEach(coleccion -> coleccion.filtrarHechos(hechosValidos));
        hechos.forEach(hechoRepository::save);
        this.coleccionMemoryRepository.actualizarColecciones(colecciones);
    }

    private List<Fuente> instanciarFuentes(){
        Fuente fuenteEstatica = new Fuente("localhost","8060");
        Fuente fuenteDinamica = new Fuente("localhost","8070");
        Fuente fuenteProxy = new Fuente("localhost","8090");
        List<Fuente> importadores = List.of(fuenteEstatica,fuenteDinamica,fuenteProxy);

        return importadores;
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



    private ColeccionOutputDTO coleccionTODTO(Coleccion coleccion) {
            ColeccionOutputDTO coleccionDTO =  new ColeccionOutputDTO();

              coleccionDTO.setTitulo(coleccion.getTitulo());
              coleccionDTO.setDescripcion(coleccion.getDescripcion());
              coleccionDTO.setImportadores(coleccion.getImportadores());
              //Falta cambiar el package de donde viene
              coleccionDTO.setCriteriosDePertenencia(coleccion.getCriteriosDePertenencia());

       return coleccionDTO;
    }


    private Coleccion dtoToColeccion(ColeccionInputDTO coleccionDTO,List<Fuente> importadores){
        return new Coleccion(
                coleccionDTO.getTitulo(),
                coleccionDTO.getDescripcion(),
                importadores,
                obtenerCriteriosDTO(coleccionDTO));
    }


}
