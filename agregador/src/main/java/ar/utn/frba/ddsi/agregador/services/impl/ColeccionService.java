package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
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

    public ColeccionService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }

//creo la coleccion
    @Override
    public List<Hecho> createColeccion(ColeccionInputDTO coleccionDTO) {
        //lo primero que hago es tomar todas las fuentes importadoras de hechos y a cada una la ionstancio
        Fuente fuenteEstatica = new Fuente("localhost", "8060", Origen.DATASET);
        Fuente fuenteDinamica = new Fuente("localhost", "8070", Origen.CONTRIBUYENTE); // o CARGA_MANUAL
        Fuente fuenteProxy = new Fuente("localhost", "8090", Origen.EXTERNO);

        List<Fuente> importadores = List.of(fuenteEstatica,fuenteDinamica,fuenteProxy);


        Coleccion coleccion = dtoToColeccion(coleccionDTO,importadores);

        List<Hecho> todosLosHechos = this.tomarHechosImportadores(importadores,coleccion.getCriteriosDePertenencia());

        //me quedo con los hechos validos
        List<Hecho> hechosValidos = filtrarHechosValidos(todosLosHechos);

        //y ahora me fijo si los hechos cumplen con los criterios de la coleccion y si es asi los meto
        List<Hecho> hechos = asignarHechosAColeccion(hechosValidos, coleccion);
        hechos.forEach(hechoRepository::save);
        return hechos;
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
        Fuente fuenteEstatica = new Fuente("localhost", "8060", Origen.DATASET);
        Fuente fuenteDinamica = new Fuente("localhost", "8070", Origen.CONTRIBUYENTE); // o CARGA_MANUAL
        Fuente fuenteProxy = new Fuente("localhost", "8090", Origen.EXTERNO);
        List<Fuente> importadores = List.of(fuenteEstatica,fuenteDinamica,fuenteProxy);
        List<Hecho> hechos = this.tomarHechosImportadores(importadores,null);
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
