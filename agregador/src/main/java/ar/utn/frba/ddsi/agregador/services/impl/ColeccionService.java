package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionMemoryRepository;
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
    private IColeccionMemoryRepository coleccionMemoryRepository;

    public ColeccionService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }

//creo la coleccion
    @Override
    public List<Hecho> createColeccion(ColeccionInputDTO coleccionDTO) {
        //Instancio las fuentes
        List<Fuente> importadores = this.instanciarFuentes();
        //ahora tomo los criterios y los instancio con la funcion mapearCriterioDTO




        List<CriterioDePertenencia> criterios = coleccionDTO.getCriterios().stream().toList();


        //creo la coleccion
        Coleccion nuevaColeccion = dtoToColeccion(coleccionDTO, importadores);

        //agarro y tomo todos los hechos de los importadores que tiene mi coleccion
        List<Hecho> todosLosHechos = this.tomarHechosImportadores(importadores, criterios);


        //TODO: ademas de filtar por criterio que ya esta hecho tomar solo los que tengan el atributo valido en true
        //List<Hecho> hechosValidos = filtrarHechosValidos(todosLosHechos);

        //y ahora me fijo si los hechos cumplen con los criterios de la coleccion y si es asi los meto
        List<Hecho> hechos = asignarHechosAColeccion(todosLosHechos,nuevaColeccion);
        hechos.forEach(hechoRepository::save);
        //agrego la coleccion a la memoria
        //TODO: PERSISTIR SOLO EL HANDLE
        this.coleccionMemoryRepository.save(nuevaColeccion);
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
        //TODO: LO SACO DEL REPOSITORY
        List<Hecho> hechos = new ArrayList<>();
        return hechos;
    }

    public void actualizarHechos(){
        List<Coleccion> colecciones = this.coleccionMemoryRepository.findAll();
        //TODO: por cada coleccion tomar sus hechos y volver a traerlos de las fuentes


        List<Hecho> hechos = this.tomarHechosImportadores(this.instanciarFuentes(), null);

        List<Hecho> hechosValidos = filtrarHechosValidos(hechos);
        colecciones.forEach(coleccion -> coleccion.filtrarHechos(hechosValidos));
        hechos.forEach(hechoRepository::save);
        //TODO: volver a guardar las colecciones con los hechos actualizados
        //hacer for each de cada coleccion y guardaR
        this.coleccionMemoryRepository.save(coleccion);
    }

    private List<Fuente> instanciarFuentes(){
        Fuente fuenteEstatica = new Fuente("localhost","8060", Origen.ESTATICO);
        Fuente fuenteDinamica = new Fuente("localhost","8070", Origen.DINAMICO);
        Fuente fuenteProxy = new Fuente("localhost","8090", Origen.EXTERNO);
        List<Fuente> importadores = List.of(fuenteEstatica,fuenteDinamica,fuenteProxy);

        return importadores;
    }




    //Esto es solo para agregar los hechos validos
    private List<Hecho> filtrarHechosValidos(List<Hecho> hechos){
       return hechos.stream().filter(Hecho::getEsValido).collect(Collectors.toList());
    }




}
