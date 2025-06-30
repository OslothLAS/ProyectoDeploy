package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import ar.utn.frba.ddsi.agregador.navegacion.NavegacionStrategy;
import ar.utn.frba.ddsi.agregador.navegacion.NavegacionStrategyFactory;
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
    private final IColeccionRepository coleccionRepository;
    private final NavegacionStrategyFactory navegacionFactory;

    public ColeccionService(IHechoRepository hechoRepository, IColeccionRepository coleccionRepository,  NavegacionStrategyFactory navegacionFactory) {
        this.hechoRepository = hechoRepository;
        this.coleccionRepository = coleccionRepository;
        this.navegacionFactory = navegacionFactory;
    }



//creo la coleccion
    @Override
    public void createColeccion(ColeccionInputDTO coleccionDTO) {

        List<Fuente> importadores = this.instanciarFuentes();

        List<CriterioDePertenencia> criterios = coleccionDTO.getCriterios().stream().toList();

        Coleccion nuevaColeccion = dtoToColeccion(coleccionDTO, importadores);

        List<Hecho> todosLosHechos = this.tomarHechosImportadores(importadores, criterios);

        List<Hecho> hechos = asignarColeccionAHechos(todosLosHechos,nuevaColeccion);
        hechos.forEach(hechoRepository::save);

        this.coleccionRepository.save(nuevaColeccion);
    }


    //aca asigno los hechos a una coleccion
    private List<Hecho> asignarColeccionAHechos(List<Hecho> hechosValidos, Coleccion coleccion) {
        return hechosValidos.stream()
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

    private List<Hecho> tomarHechosDeColeccion(Coleccion coleccion) {
        // 1. Obtener todos los hechos que tienen esta colección en su lista de colecciones
        return hechoRepository.findAll().stream()
                .filter(hecho -> hecho.getColecciones() != null)
                .filter(hecho -> hecho.getColecciones().contains(coleccion))
                .collect(Collectors.toList());
        }

    @Override
    public List<Hecho> getColeccion(String idColeccion, String modoNavegacion) {
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion);
        List<Hecho> hechosDeColeccion = tomarHechosDeColeccion(coleccion);

        NavegacionStrategy strategy = navegacionFactory.getStrategy(modoNavegacion);

        // Aplica la estrategia
        return strategy.navegar(coleccion, hechosDeColeccion);
    }

    public void actualizarHechos(){
        List<Coleccion> colecciones = this.coleccionRepository.findAll();

        colecciones.forEach(coleccion -> {
            List<CriterioDePertenencia> criterios = coleccion.getCriteriosDePertenencia().stream().toList();
            List<Hecho> hechos = tomarHechosImportadores(instanciarFuentes(), criterios);
            asignarColeccionAHechos(hechos, coleccion);
            hechos.forEach(hechoRepository::save);
            coleccionRepository.save(coleccion);
        });
    }

    private List<Fuente> instanciarFuentes(){
        Fuente fuenteEstatica = new Fuente("localhost","8060", Origen.ESTATICO);
        Fuente fuenteDinamica = new Fuente("localhost","8070", Origen.DINAMICO);
        Fuente fuenteProxy = new Fuente("localhost","8090", Origen.EXTERNO);

        return List.of(fuenteEstatica,fuenteDinamica,fuenteProxy);
    }



    //TODO: esto tiene que esdtar en cada fuente
    private List<Hecho> filtrarHechosValidos(List<Hecho> hechos){
       return hechos.stream().filter(Hecho::getEsValido).collect(Collectors.toList());
    }


    public void consensuarHechos(){
        List<Coleccion> colecciones =  this.coleccionRepository.findAll();
        List <Hecho> hechosAsignados = new ArrayList<>();

        colecciones.forEach(c -> {
            List<Hecho> hechosConsensuados = c.getConsensoStrategy()
                    .obtenerHechosConsensuados(c.getImportadores(), tomarHechosDeColeccion(c));

            List<Hecho> hechosAsignadosPorColeccion = asignarColeccionAHechos(hechosConsensuados, c);

            hechosAsignados.addAll(hechosAsignadosPorColeccion);
        });

        hechosAsignados.forEach(hechoRepository::save); //aca actualizo los ids de colecciones
    }

}
