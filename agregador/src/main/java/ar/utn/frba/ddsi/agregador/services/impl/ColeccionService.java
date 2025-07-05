package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import ar.utn.frba.ddsi.agregador.navegacion.NavegacionStrategy;
import ar.utn.frba.ddsi.agregador.navegacion.NavegacionStrategyFactory;
import entities.colecciones.Coleccion;
import entities.colecciones.Fuente;
import entities.colecciones.consenso.strategies.ConsensoAbsolutaStrategy;
import entities.colecciones.consenso.strategies.ConsensoMayoriaStrategy;
import entities.colecciones.consenso.strategies.ConsensoMultipleMencionStrategy;
import entities.colecciones.consenso.strategies.ConsensoStrategy;
import entities.colecciones.consenso.strategies.TipoConsenso;
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

    private final Map<TipoConsenso, ConsensoStrategy> estrategias = new HashMap<>();


    public ColeccionService(IHechoRepository hechoRepository, IColeccionRepository coleccionRepository) {
        this.hechoRepository = hechoRepository;
        this.coleccionRepository = coleccionRepository;
    }



//creo la coleccion
    @Override
    public void createColeccion(ColeccionInputDTO coleccionDTO) {

        List<Fuente> importadores = this.instanciarFuentes();
        List<CriterioDePertenencia> criterios = new ArrayList<>();

        if(coleccionDTO.getCriterios() != null && !coleccionDTO.getCriterios().isEmpty()) {
            criterios = coleccionDTO.getCriterios().stream().toList();
        }
        Coleccion nuevaColeccion = dtoToColeccion(coleccionDTO, importadores);

        List<Hecho> todosLosHechos = this.tomarHechosImportadores(importadores, criterios);

        List<Hecho> hechos = asignarColeccionAHechos(todosLosHechos,nuevaColeccion);
        hechos.forEach(hechoRepository::save);

        this.coleccionRepository.save(nuevaColeccion);
    }


    //aca asigno los hechos a una coleccionockitoExtension.
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
    public List<Hecho> getColeccion(Long idColeccion, String modoNavegacion) {
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idColeccion));

        List<Hecho> hechosDeColeccion = tomarHechosDeColeccion(coleccion);
        NavegacionStrategy strategy = NavegacionStrategyFactory.getStrategy(modoNavegacion);

        return strategy.navegar(coleccion, hechosDeColeccion);
    }

    @Override
    public boolean deleteColeccion(Long idColeccion) {
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion)
            .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idColeccion));

        return this.coleccionRepository.delete(idColeccion);
    }

    @Override
    public void cambiarConsenso(Long idColeccion, TipoConsenso tipo) {
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion)
            .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idColeccion));

        switch (tipo) {
            case ABSOLUTA -> coleccion.setConsensoStrategy(new ConsensoAbsolutaStrategy());
            case MAYORIA -> coleccion.setConsensoStrategy(new ConsensoMayoriaStrategy());
            case MULTIPLE_MENCION -> coleccion.setConsensoStrategy(new ConsensoMultipleMencionStrategy());
        }
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


        return List.of(fuenteEstatica,fuenteDinamica);
    }



    //TODO: esto tiene que esdtar en cada fuente
    private List<Hecho> filtrarHechosValidos(List<Hecho> hechos){
       return hechos.stream().filter(Hecho::getEsValido).collect(Collectors.toList());
    }

    @Override

    public void consensuarHechos(){
        List<Coleccion> colecciones =  this.coleccionRepository.findAll();
        List <Hecho> hechosAsignados = new ArrayList<>();

        colecciones.forEach(c -> {
            List<Hecho> hechosConsensuados = c.getConsensoStrategy()
                    .obtenerHechosConsensuados(c.getImportadores(), tomarHechosDeColeccion(c));

            List<Hecho> hechosAsignadosPorColeccion = asignarColeccionAHechos(hechosConsensuados, c);
            hechosAsignadosPorColeccion.forEach(h->h.setEsConsensuado(true));

            List<Hecho> hechosActualmenteAsociados = tomarHechosDeColeccion(c);

            hechosActualmenteAsociados.stream()
                    .filter(Hecho::getEsConsensuado)
                    .filter(hecho -> !hechosConsensuados.contains(hecho))
                    .forEach(hecho -> hecho.setEsConsensuado(false));

            hechosAsignados.addAll(hechosAsignadosPorColeccion);
        });
        hechosAsignados.forEach(hechoRepository::save);
    }

}
