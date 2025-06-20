package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.ColeccionOutputDTO;
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

import static ar.utn.frba.ddsi.agregador.utils.ColeccionUtil.coleccionToDto;
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
    public void createColeccion(ColeccionInputDTO coleccionDTO) {

        List<Fuente> importadores = this.instanciarFuentes();

        List<CriterioDePertenencia> criterios = coleccionDTO.getCriterios().stream().toList();

        Coleccion nuevaColeccion = dtoToColeccion(coleccionDTO, importadores);

        List<Hecho> todosLosHechos = this.tomarHechosImportadores(importadores, criterios);

        List<Hecho> hechos = asignarHechosAColeccion(todosLosHechos,nuevaColeccion);
        hechos.forEach(hechoRepository::save);

        this.coleccionMemoryRepository.save(nuevaColeccion);
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
    public ColeccionOutputDTO getColeccion(String idColeccion) {
        Coleccion coleccion = this.coleccionMemoryRepository.findById(idColeccion);
        return coleccionToDto(coleccion);
    }

    public void actualizarHechos(){
        List<Coleccion> colecciones = this.coleccionMemoryRepository.findAll();

        colecciones.forEach(coleccion -> {
            List<CriterioDePertenencia> criterios = coleccion.getCriteriosDePertenencia().stream().toList();
            List<Hecho> hechos = tomarHechosImportadores(instanciarFuentes(), criterios);
            asignarHechosAColeccion(hechos, coleccion);
            hechos.forEach(hechoRepository::save);
            coleccionMemoryRepository.save(coleccion);
        });
    }

    private List<Fuente> instanciarFuentes(){
        Fuente fuenteEstatica = new Fuente("localhost","8060", Origen.ESTATICO);
        Fuente fuenteDinamica = new Fuente("localhost","8070", Origen.DINAMICO);
        Fuente fuenteProxy = new Fuente("localhost","8090", Origen.EXTERNO);
        List<Fuente> importadores = List.of(fuenteEstatica,fuenteDinamica,fuenteProxy);

        return importadores;
    }



    //TODO: esto tiene que esdtar en cada fuente
    private List<Hecho> filtrarHechosValidos(List<Hecho> hechos){
       return hechos.stream().filter(Hecho::getEsValido).collect(Collectors.toList());
    }




}
