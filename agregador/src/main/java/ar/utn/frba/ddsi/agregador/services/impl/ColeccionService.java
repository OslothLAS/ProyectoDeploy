package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.FuenteInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.ICategoriaRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IUbicacionRepository;
import ar.utn.frba.ddsi.agregador.navegacion.NavegacionStrategy;
import ar.utn.frba.ddsi.agregador.navegacion.NavegacionStrategyFactory;
import entities.colecciones.Coleccion;
import entities.colecciones.Fuente;
import entities.colecciones.consenso.strategies.Absoluta;
import entities.colecciones.consenso.strategies.Mayoria;
import entities.colecciones.consenso.strategies.MultipleMencion;
import entities.colecciones.consenso.strategies.TipoConsenso;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.criteriosDePertenencia.CriterioPorCategoria;
import entities.hechos.*;
import jakarta.transaction.Transactional;
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
    private final IUbicacionRepository ubicacionRepository;

    public ColeccionService(IHechoRepository hechoRepository, IColeccionRepository coleccionRepository, ICategoriaRepository categoriaRepository, IUbicacionRepository provinciaRepository) {
        this.hechoRepository = hechoRepository;
        this.coleccionRepository = coleccionRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = provinciaRepository;
    }

    public Categoria obtenerOCrearCategoria(String nombre) {
        String clave = normalizarTrimTexto(nombre);
        return categoriaRepository.findByCategoriaNormalizada(clave)
                .orElseGet(() -> categoriaRepository.save(new Categoria(nombre)));
    }


    @Transactional
    public void createColeccion(ColeccionInputDTO coleccionDTO) {
        List<Fuente> importadores = coleccionDTO.getFuentes();
        List<CriterioDePertenencia> criterios = this.obtenerCriterios(coleccionDTO.getCriterios());

        Coleccion nuevaColeccion = dtoToColeccion(coleccionDTO, importadores);
        nuevaColeccion.setCriteriosDePertenencia(criterios);
        this.coleccionRepository.save(nuevaColeccion);

        List<Hecho> hechos = this.procesarHechos(importadores, criterios, nuevaColeccion);

        this.hechoRepository.saveAll(hechos); //ver si con null se puede, supongo que si
    }

    private List<CriterioDePertenencia> obtenerCriterios(List<CriterioDePertenencia> criteriosOriginal) {
        if (criteriosOriginal == null || criteriosOriginal.isEmpty()) {
            return new ArrayList<>();
        }

        List<CriterioDePertenencia> criteriosProcesados = new ArrayList<>();

        for (CriterioDePertenencia criterio : criteriosOriginal) {
            if (criterio instanceof CriterioPorCategoria catCriterio) {
                String nombreCategoria = catCriterio.getCategoria().getCategoria();
                Categoria categoriaExistente = obtenerOCrearCategoria(nombreCategoria);
                catCriterio.setCategoria(categoriaExistente);
            }
            criteriosProcesados.add(criterio);
        }

        return criteriosProcesados;
    }

    private List<Hecho> procesarHechos(List<Fuente> fuentes, List<CriterioDePertenencia> criterios, Coleccion coleccion) {
        List<Hecho> todosLosHechos = fuentes.parallelStream()
                .flatMap(fuente ->
                        fuente.obtenerHechos(criterios).stream()
                                .peek(hecho -> hecho.setFuenteOrigen(fuente.getOrigenHechos()))
                )
                .collect(Collectors.toList());

        if (todosLosHechos.isEmpty()) {
            return new ArrayList<>();
        }

        normalizarHechos(todosLosHechos);
        return todosLosHechos.stream()
                .peek(h -> h.addColeccion(coleccion))
                .collect(Collectors.toList());
    }

    public void normalizarHechos(List<Hecho> hechos){
        Map<String, Ubicacion> ubicacionesPorClave = new HashMap<>();

        hechos.parallelStream().forEach(Hecho::normalizarHecho);

        Set<String> clavesUbicacion = new HashSet<>();
        for (Hecho hecho : hechos) {
            if (hecho.getDatosHechos().getUbicacion() != null) {
                Ubicacion ub = hecho.getDatosHechos().getUbicacion();
                String clave = ub.getLatitud() + "," + ub.getLongitud();
                clavesUbicacion.add(clave);
                ubicacionesPorClave.put(clave, ub);
            }
        }

        if (!clavesUbicacion.isEmpty()) {
            List<Ubicacion> ubicacionesExistentes = ubicacionRepository.findByLatitudAndLongitudIn(clavesUbicacion);

            for (Ubicacion ubi : ubicacionesExistentes) {
                String clave = ubi.getLatitud() + "," + ubi.getLongitud();
                ubicacionesPorClave.put(clave, ubi);
            }

            List<Ubicacion> nuevasUbicaciones = ubicacionesPorClave.values().stream()
                    .filter(ub -> ub.getId() == null)
                    .collect(Collectors.toList());

            if (!nuevasUbicaciones.isEmpty()) {
                List<Ubicacion> guardadas = ubicacionRepository.saveAll(nuevasUbicaciones);

                for (Ubicacion guardada : guardadas) {
                    String clave = guardada.getLatitud() + "," + guardada.getLongitud();
                    ubicacionesPorClave.put(clave, guardada);
                }
            }
        }

        for (Hecho hecho : hechos) {
            if (hecho.getDatosHechos().getUbicacion() != null) {
                Ubicacion ub = hecho.getDatosHechos().getUbicacion();
                String clave = ub.getLatitud() + "," + ub.getLongitud();
                hecho.getDatosHechos().setUbicacion(ubicacionesPorClave.get(clave));
            }

            String nombreCategoria = hecho.getDatosHechos().getCategoria().getCategoria();
            Categoria categoriaExistente = obtenerOCrearCategoria(nombreCategoria);
            hecho.getDatosHechos().setCategoria(categoriaExistente);
        }
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

            this.normalizarHechos(hechos);

            asignarColeccionAHechos(hechos, coleccion);
            hechoRepository.saveAll(hechos);
            coleccionRepository.save(coleccion);
        });
    }

    @Override
    public void consensuarHechos() {
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

    public List<StatDTO> getProvinciaMasReportadaPorTodasLasColecciones() {
        return this.hechoRepository.countHechosByProvinciaAndColeccion();
    }

    public List<StatDTO> getCategoriaMasReportada(){
        return hechoRepository.findCategoriaWithMostHechos();
    }

    public List<StatDTO> getHoraMasReportada(){
        return hechoRepository.findHoraWithMostHechosByCategoria();
    }

    public List<StatDTO> getProviniciaMasReportadaPorCategoria(){
        return hechoRepository.findProvinciaWithMostHechosByCategoria();
    }
}
