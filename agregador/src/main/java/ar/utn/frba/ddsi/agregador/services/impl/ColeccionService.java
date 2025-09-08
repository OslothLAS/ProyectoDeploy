package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.FuenteInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.ICategoriaRepository;
import ar.utn.frba.ddsi.agregador.navegacion.NavegacionStrategy;
import ar.utn.frba.ddsi.agregador.navegacion.NavegacionStrategyFactory;
import entities.colecciones.Coleccion;
import entities.colecciones.Fuente;
import entities.colecciones.consenso.strategies.Absoluta;
import entities.colecciones.consenso.strategies.Mayoria;
import entities.colecciones.consenso.strategies.MultipleMencion;
import entities.colecciones.consenso.strategies.TipoConsenso;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.hechos.Categoria;
import entities.hechos.Hecho;
import org.springframework.stereotype.Service;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import java.util.*;
import java.util.stream.Collectors;
import static ar.utn.frba.ddsi.agregador.utils.ColeccionUtil.dtoToColeccion;
import static ar.utn.frba.ddsi.agregador.utils.ColeccionUtil.fuenteDTOtoFuente;
import static utils.NormalizadorTexto.normalizarTrimTexto;

@Service
public class ColeccionService implements IColeccionService {

    private final IHechoRepository hechoRepository;
    private final IColeccionRepository coleccionRepository;
    private final ICategoriaRepository categoriaRepository;

    public ColeccionService(IHechoRepository hechoRepository, IColeccionRepository coleccionRepository, ICategoriaRepository categoriaRepository) {
        this.hechoRepository = hechoRepository;
        this.coleccionRepository = coleccionRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria obtenerOCrear(String nombre) {
        String clave = normalizarTrimTexto(nombre);
        return categoriaRepository.findByCategoriaNormalizada(clave)
                .orElseGet(() -> categoriaRepository.save(new Categoria(nombre)));
    }

    @Override
    public void createColeccion(ColeccionInputDTO coleccionDTO) {

        List<Fuente> importadores = coleccionDTO.getFuentes();
        List<CriterioDePertenencia> criterios = new ArrayList<>();

        if(coleccionDTO.getCriterios() != null && !coleccionDTO.getCriterios().isEmpty()) {
            criterios = coleccionDTO.getCriterios().stream().toList();
        }
        Coleccion nuevaColeccion = dtoToColeccion(coleccionDTO, importadores);

        List<Hecho> todosLosHechos = this.tomarHechosFuentes(importadores, criterios);

        Set<String> nombresCategorias = todosLosHechos.stream()
                .map(h -> h.getDatosHechos().getCategoria().getCategoria())
                .collect(Collectors.toSet());

        Map<String, Categoria> categoriaMap = nombresCategorias.stream()
                .map(this::obtenerOCrear)
                .collect(Collectors.toMap(Categoria::getCategoriaNormalizada, c -> c));

        todosLosHechos.forEach(h -> {
            String clave = h.getDatosHechos().getCategoria().getCategoriaNormalizada();
            Categoria cat = categoriaMap.get(clave);
            h.getDatosHechos().setCategoria(cat);
        });

        List<Hecho> hechos = asignarColeccionAHechos(todosLosHechos,nuevaColeccion);

        this.coleccionRepository.save(nuevaColeccion);
        this.hechoRepository.saveAll(hechos);
    }

    public List<Coleccion> getColecciones(){
        return this.coleccionRepository.findAll();
    }

    private List<Hecho> asignarColeccionAHechos(List<Hecho> hechosValidos, Coleccion coleccion) {
        return hechosValidos.stream()
                .peek(h -> h.addColeccion(coleccion))
                .toList();
    }

    private List<Hecho> tomarHechosFuentes(List<Fuente> fuentes, List<CriterioDePertenencia> criterios) {
        return fuentes.stream()
            .flatMap(fuente ->
                    fuente.obtenerHechos(criterios).stream()
                            .peek(hecho -> hecho.setFuenteOrigen(fuente.getOrigenHechos()))
            )
            .toList();
    }

    private List<Hecho> tomarHechosDeColeccion(Coleccion coleccion) {
        return hechoRepository.findAll().stream()
                .filter(hecho -> hecho.getColecciones() != null)
                .filter(hecho -> hecho.getColecciones().stream().anyMatch(c -> c.getHandle().equals(coleccion.getHandle())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Hecho> getHechosDeColeccion(Long idColeccion, String modoNavegacion) {
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idColeccion));

        List<Hecho> hechosDeColeccion = tomarHechosDeColeccion(coleccion);
        NavegacionStrategy strategy = NavegacionStrategyFactory.getStrategy(modoNavegacion);

        return strategy.navegar(coleccion, hechosDeColeccion);
    }

    @Override
    public void deleteColeccion(Long idColeccion) {
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idColeccion));
        this.coleccionRepository.delete(coleccion);
    }

    @Override
    public void cambiarConsenso(Long idColeccion, TipoConsenso tipo) {
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion)
            .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idColeccion));

        switch (tipo) {
            case ABSOLUTA -> coleccion.setConsenso(new Absoluta());
            case MAYORIA -> coleccion.setConsenso(new Mayoria());
            case MULTIPLE_MENCION -> coleccion.setConsenso(new MultipleMencion());
        }
    }

    @Override
    public void agregarFuente(Long idColeccion, FuenteInputDTO fuenteDTO) {
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion)
            .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idColeccion));

        coleccion.agregarImportador(fuenteDTOtoFuente(fuenteDTO));
    }

    @Override
    public void eliminarFuente(Long idColeccion, Long idFuente) {
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion)
            .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idColeccion));

        coleccion.getImportadores().removeIf(fuente -> Objects.equals(fuente.getId(), idFuente));
    }

    @Override
    public void actualizarHechos(){
        List<Coleccion> colecciones = this.coleccionRepository.findAll();

        colecciones.forEach(coleccion -> {
            List<CriterioDePertenencia> criterios = Optional.ofNullable(coleccion.getCriteriosDePertenencia())
                    .orElse(List.of());
            List<Hecho> hechos = tomarHechosFuentes(coleccion.getImportadores(), criterios);
            asignarColeccionAHechos(hechos, coleccion);
            hechoRepository.saveAll(hechos);
            coleccionRepository.save(coleccion);
        });
    }

    @Override
    public void consensuarHechos(){
        List<Coleccion> colecciones =  this.coleccionRepository.findAll();
        List <Hecho> hechosAsignados = new ArrayList<>();

        colecciones.forEach(c -> {
            List<Hecho> hechosConsensuados = c.getConsenso()
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
        hechoRepository.saveAll(hechosAsignados);
    }

    public List<StatDTO> getHechosProvincia(Long idColeccion){

        return new ArrayList<>();
    }
}
