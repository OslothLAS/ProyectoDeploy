package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.utils.HechoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ResponseStatusException;
import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.FuenteInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.*;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.consenso.strategies.*;
import ar.utn.frba.ddsi.agregador.models.entities.factories.ConsensoFactory;
import ar.utn.frba.ddsi.agregador.models.entities.factories.CriterioDePertenenciaFactory;
import ar.utn.frba.ddsi.agregador.models.repositories.*;
import ar.utn.frba.ddsi.agregador.navegacion.NavegacionStrategy;
import ar.utn.frba.ddsi.agregador.navegacion.NavegacionStrategyFactory;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioPorCategoria;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.*;
import ar.utn.frba.ddsi.agregador.utils.ColeccionUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.utn.frba.ddsi.agregador.utils.ColeccionUtil.dtoToColeccion;
import static ar.utn.frba.ddsi.agregador.utils.ColeccionUtil.fuenteDTOtoFuente;
import static ar.utn.frba.ddsi.agregador.utils.HechoUtil.*;
import static ar.utn.frba.ddsi.agregador.utils.NormalizadorTexto.normalizarTrimTexto;

@Service
public class ColeccionService implements IColeccionService {

    private final IHechoRepository hechoRepository;
    private final IColeccionRepository coleccionRepository;
    private final ICategoriaRepository categoriaRepository;
    private final IUbicacionRepository ubicacionRepository;
    private final IProvinciaRepository provinciaRepository;
    private final ILocalidadRepository localidadRepository;
    private final IFuenteRepository fuenteRepository;
    private final ICriterioRepository criterioRepository;
    private static final Logger log = LoggerFactory.getLogger(ColeccionService.class);
    @Autowired
    @Qualifier("hechoWebClient")
    private WebClient hechoWebClient;

    private final GeorefLocalCache georefLocalCache;

    public ColeccionService(IHechoRepository hechoRepository, IColeccionRepository coleccionRepository,
                            ICategoriaRepository categoriaRepository, IUbicacionRepository ubicacionRepository,
                            IProvinciaRepository provinciaRepository, ILocalidadRepository localidadRepository,
                            IFuenteRepository fuenteRepository, ICriterioRepository criterioRepository, GeorefLocalCache georefLocalCache) {
        this.hechoRepository = hechoRepository;
        this.coleccionRepository = coleccionRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.provinciaRepository = provinciaRepository;
        this.localidadRepository = localidadRepository;
        this.fuenteRepository = fuenteRepository;
        this.criterioRepository = criterioRepository;
        this.georefLocalCache = georefLocalCache;
    }

    @Transactional
    public void normalizarHechos(List<Hecho> hechos) {
        Map<String, Ubicacion> ubicacionesBD = ubicacionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        u -> u.getLatitud() + "," + u.getLongitud(),
                        u -> u
                ));

        Map<String, Provincia> provinciasBD = provinciaRepository.findAll().stream()
                .collect(Collectors.toMap(Provincia::getNombre, p -> p));

        Map<String, Localidad> localidadesBD = localidadRepository.findAll().stream()
                .collect(Collectors.toMap(
                        l -> l.getNombre() + "_" + l.getProvincia().getNombre(),
                        l -> l
                ));

        ConcurrentHashMap<String, Ubicacion> ubicCache = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Provincia> provCache = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Localidad> locCache = new ConcurrentHashMap<>();

        hechos.parallelStream().forEach(hecho -> {
            String lat = hecho.getUbicacion().getLatitud();
            String lon = hecho.getUbicacion().getLongitud();
            String key = lat + "," + lon;

            if (ubicacionesBD.containsKey(key)) {
                hecho.setUbicacion(ubicacionesBD.get(key));
            } else {
                Ubicacion buscada = georefLocalCache.buscar(lat, lon);
                Localidad locGeo = buscada.getLocalidad();

                ubicCache.put(key, buscada);
                if (locGeo.getProvincia() != null) {
                    provCache.put(locGeo.getProvincia().getNombre(), locGeo.getProvincia());
                }
                String locKey = locGeo.getNombre() + "_" +
                        (locGeo.getProvincia() != null ? locGeo.getProvincia().getNombre() : "");
                locCache.put(locKey, locGeo);
            }

            String catNombre = hecho.getCategoria().getCategoria();
            Categoria cat = obtenerOCrearCategoriaCached(catNombre);
            hecho.setCategoria(cat);
        });

        List<Provincia> provinciasNuevas = provCache.values().stream()
                .filter(p -> !provinciasBD.containsKey(p.getNombre()))
                .distinct()
                .collect(Collectors.toList());

        if (!provinciasNuevas.isEmpty()) {
            List<Provincia> provinciasGuardadas = provinciaRepository.saveAll(provinciasNuevas);
            provinciasGuardadas.forEach(p -> provinciasBD.put(p.getNombre(), p));
        }

        List<Localidad> localidadesNuevas = locCache.values().stream()
                .filter(l -> {
                    String locKey = l.getNombre() + "_" + l.getProvincia().getNombre();
                    return !localidadesBD.containsKey(locKey);
                })
                .peek(l -> {
                    Provincia provPersistida = provinciasBD.get(l.getProvincia().getNombre());
                    l.setProvincia(provPersistida);
                })
                .distinct()
                .collect(Collectors.toList());

        if (!localidadesNuevas.isEmpty()) {
            List<Localidad> localidadesGuardadas = localidadRepository.saveAll(localidadesNuevas);
            localidadesGuardadas.forEach(l -> {
                String locKey = l.getNombre() + "_" + l.getProvincia().getNombre();
                localidadesBD.put(locKey, l);
            });
        }

        List<Ubicacion> ubicacionesNuevas = ubicCache.values().stream()
                .filter(u -> {
                    String key = u.getLatitud() + "," + u.getLongitud();
                    return !ubicacionesBD.containsKey(key);
                })
                .peek(u -> {
                    Localidad loc = u.getLocalidad();
                    String locKey = loc.getNombre() + "_" + loc.getProvincia().getNombre();
                    Localidad locPersistida = localidadesBD.get(locKey);
                    u.setLocalidad(locPersistida);
                })
                .distinct()
                .collect(Collectors.toList());

        if (!ubicacionesNuevas.isEmpty()) {
            List<Ubicacion> ubicacionesGuardadas = ubicacionRepository.saveAll(ubicacionesNuevas);
            ubicacionesGuardadas.forEach(u -> {
                String key = u.getLatitud() + "," + u.getLongitud();
                ubicacionesBD.put(key, u);
            });
        }

        hechos.forEach(hecho -> {
            String key = hecho.getUbicacion().getLatitud() + "," +
                    hecho.getUbicacion().getLongitud();
            Ubicacion ubicPersistida = ubicacionesBD.get(key);
            if (ubicPersistida != null) {
                hecho.setUbicacion(ubicPersistida);
            }
        });
    }

    private final Map<String, Categoria> categoriaCache = new ConcurrentHashMap<>();

    private Categoria obtenerOCrearCategoriaCached(String nombre) {
        String clave = normalizarTrimTexto(nombre);

        return categoriaCache.computeIfAbsent(clave, k ->
                categoriaRepository.findByCategoriaNormalizada(k)
                        .orElseGet(() -> categoriaRepository.save(new Categoria(nombre)))
        );
    }

    public Categoria obtenerOCrearCategoria(String nombre) {
        String clave = normalizarTrimTexto(nombre);
        return categoriaRepository.findByCategoriaNormalizada(clave)
                .orElseGet(() -> categoriaRepository.save(new Categoria(nombre)));
    }


    public List<Coleccion> getColeccionesClass(){
        return this.coleccionRepository.findAll();
   }

    private List<Hecho> filtrarHechosRepetidosOptimizado(List<Hecho> hechosNuevos) {
        if (hechosNuevos.isEmpty()) {
            return hechosNuevos;
        }

        Set<String> titulos = hechosNuevos.stream()
                .map(Hecho::getTitulo)
                .collect(Collectors.toSet());

        Set<String> descripciones = hechosNuevos.stream()
                .map(Hecho::getDescripcion)
                .collect(Collectors.toSet());

        Set<String> fuentes = hechosNuevos.stream()
                .map(h -> h.getFuenteOrigen().name())
                .collect(Collectors.toSet());

        List<Hecho> hechosExistentes = hechoRepository.findPosiblesCoincidencias(
                new ArrayList<>(titulos),
                new ArrayList<>(descripciones),
                new ArrayList<>(fuentes)
        );

        Map<String, Hecho> existentesPorClave = new HashMap<>();
        for (Hecho h : hechosExistentes) {
            String clave = generarClave(h.getTitulo(), h.getDescripcion()) + "_" + h.getFuenteOrigen();
            existentesPorClave.put(clave, h);
        }

        List<Hecho> result = new ArrayList<>();
        Set<String> clavesYaProcesadas = new HashSet<>();

        for (Hecho h : hechosNuevos) {
            String clave = generarClave(h.getTitulo(), h.getDescripcion()) + "_" + h.getFuenteOrigen();

            if (clavesYaProcesadas.contains(clave)) {
                continue;
            }

            result.add(existentesPorClave.getOrDefault(clave, h));
            clavesYaProcesadas.add(clave);
        }

        return result;
    }

    @Transactional
    public void createColeccion(ColeccionInputDTO coleccionDTO) {

        StopWatch sw = new StopWatch();

        sw.start("existsByTitulo");
        if (coleccionRepository.existsByTitulo(coleccionDTO.getTitulo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una colección con ese título");
        }
        sw.stop();

        sw.start("buscar fuentes");
        List<String> puertos = coleccionDTO.getFuentes().stream().map(Fuente::getPuerto).toList();
        List<Fuente> fuentesExistentes = fuenteRepository.findAllByPuertoIn(puertos);
        sw.stop();

        sw.start("crear fuentes nuevas");
        Map<String, Fuente> fuentesMap = fuentesExistentes.stream()
                .collect(Collectors.toMap(Fuente::getPuerto, f -> f));
        List<Fuente> fuentesFinales = coleccionDTO.getFuentes().stream()
                .map(f -> fuentesMap.getOrDefault(f.getPuerto(), fuenteRepository.save(f)))
                .toList();
        sw.stop();

        sw.start("obtener criterios");
        List<CriterioDePertenencia> criterios = this.obtenerCriterios(coleccionDTO.getCriterios());
        sw.stop();

        sw.start("crear coleccion");
        Coleccion nuevaColeccion = dtoToColeccion(coleccionDTO, fuentesFinales);
        nuevaColeccion.setCriteriosDePertenencia(criterios);
        this.coleccionRepository.save(nuevaColeccion);
        sw.stop();

        sw.start("procesar hechos");
        List<Hecho> hechos = this.procesarHechos(fuentesFinales, criterios, nuevaColeccion);
        sw.stop();

        sw.start("filtrar repetidos");
        List<Hecho> hechosAGuardar = filtrarHechosRepetidosOptimizado(hechos);
        sw.stop();

        sw.start("asignar y guardar hechos");
        this.asignarColeccionAHechos(hechosAGuardar, nuevaColeccion);
        this.hechoRepository.saveAll(hechosAGuardar);
        sw.stop();

        log.info("\n{}", sw.prettyPrint());
    }

    @Override
    public ColeccionOutputDTO getColeccionById(Long idColeccion) {
        return this.coleccionRepository.findById(idColeccion)
                .map(ColeccionUtil::coleccionToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con id " + idColeccion));
    }

    @Override
    public ColeccionOutputDTO editarColeccion(Long idColeccion, ColeccionInputDTO dto){
        Coleccion coleccion = coleccionRepository.findById(idColeccion)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        // Actualizar campos básicos
        if (dto.getTitulo() != null) {
            coleccion.setTitulo(dto.getTitulo());
        }

        if (dto.getDescripcion() != null) {
            coleccion.setDescripcion(dto.getDescripcion());
        }

        // Actualizar estrategia de consenso usando el factory
        if (dto.getEstrategiaConsenso() != null) {
            IAlgoritmoConsenso algoritmo = ConsensoFactory.getStrategy(dto.getEstrategiaConsenso());
            coleccion.setConsenso(algoritmo);
        }

        // Actualizar fuentes si vienen
        if (dto.getFuentes() != null && !dto.getFuentes().isEmpty()) {
            coleccion.setImportadores(
                    dto.getFuentes().stream()
                            .map(f -> new Fuente(f.getIp(), f.getPuerto(), f.getId()))
                            .collect(Collectors.toList())
            );
        }

        // 🚫 No se actualizan criterios

        // Guardar cambios
        Coleccion actualizada = coleccionRepository.save(coleccion);

        // Devolver DTO de salida
        return ColeccionUtil.coleccionToDto(actualizada);
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
                .flatMap(fuente -> {
                    try {
                        List<Hecho> hechosFuente = fuente.obtenerHechos(criterios);
                        if (hechosFuente == null) {
                                System.out.println("⚠️ Fuente " + fuente.getIp() + ":" + fuente.getPuerto() + " devolvió null");
                            return Stream.<Hecho>empty();
                        }
                        hechosFuente.forEach(h -> h.setFuenteOrigen(fuente.getOrigenHechos()));
                        hechosFuente.forEach(h -> h.setEsConsensuado(false));
                        return hechosFuente.stream();
                    } catch (Exception e) {
                        System.out.println("💥 Error al obtener hechos de " + fuente.getIp() + ":" + fuente.getPuerto());
                        e.printStackTrace();
                        return Stream.<Hecho>empty();
                    }
                })
                .collect(Collectors.toList());

        if (todosLosHechos.isEmpty()) {
            return new ArrayList<>();
        }

        normalizarHechos(todosLosHechos);
        return todosLosHechos;
    }


    public List<ColeccionOutputDTO> getColecciones(){
        return this.coleccionRepository.findAll().stream().map(ColeccionUtil::coleccionToDto).collect(Collectors.toList());
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
    public List<Hecho> getHechosDeColeccion(Long idColeccion, String modoNavegacion, Map<String, String> filtros) {
        List<Hecho> hechosDeColeccion = hechoRepository.findByColeccionIdAndEsValido(idColeccion);
        NavegacionStrategy strategy = NavegacionStrategyFactory.getStrategy(modoNavegacion);

        if (filtros.containsKey("fuente")) {
            String puertoFuente = filtros.get("fuente");
            Fuente fuente = this.fuenteRepository.findByPuerto(puertoFuente);
            if (fuente != null ) {
                hechosDeColeccion = hechosDeColeccion.stream().filter(h-> h.getFuenteOrigen().equals(fuente.getOrigenHechos())).collect(Collectors.toList());
            }
        }

        List<CriterioDePertenencia> criterios = CriterioDePertenenciaFactory.crearCriterios(filtros);

        hechosDeColeccion = hechosDeColeccion.stream()
                .filter(Hecho::getEsValido)
                .filter(hecho -> criterios.isEmpty() ||
                        criterios.stream().allMatch(c -> c.cumpleCriterio(hecho)))
                .collect(Collectors.toList());

        return strategy.navegar(hechosDeColeccion);
    }

    @Override
    @Transactional
    public void deleteColeccion(Long idColeccion) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idColeccion));

        // 1️⃣ Buscar todos los hechos que contienen esta colección
        List<Hecho> hechosAsociados = hechoRepository.findByColeccionesContaining(coleccion);

        // 2️⃣ Quitar la colección de cada hecho
        for (Hecho hecho : hechosAsociados) {
            hecho.getColecciones().remove(coleccion);
        }

        // 3️⃣ Guardar los cambios en los hechos
        hechoRepository.saveAll(hechosAsociados);

        // 4️⃣ Ahora sí, eliminar la colección
        coleccionRepository.delete(coleccion);
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


    @Transactional
    @Override
    public void actualizarHechos(){
        List<Coleccion> colecciones = this.coleccionRepository.findAll();

        colecciones.forEach(coleccion -> {
            List<CriterioDePertenencia> criterios = Optional.ofNullable(coleccion.getCriteriosDePertenencia())
                    .orElse(List.of());
          
            List<Hecho> hechosDeFuentes = tomarHechosFuentes(coleccion.getImportadores(), criterios);
            List<Hecho> hechosRepository = hechoRepository.findAll();
            List<Hecho> hechosSinRepetir = filtrarHechosRepetidosCron(hechosRepository, hechosDeFuentes);
            List <Hecho> hechosAGuardar = filtrarHechosRepetidosOptimizado(hechosSinRepetir);
            this.normalizarHechos(hechosDeFuentes);

            hechoRepository.saveAll(hechosAGuardar);
            asignarColeccionAHechos(hechosAGuardar, coleccion);
            coleccionRepository.save(coleccion);
        });
    }

    private List<Hecho> filtrarHechosRepetidosCron(List<Hecho> hechosExistentes, List<Hecho> hechosNuevos) {
        return hechosNuevos.stream()
                .filter(hNuevo -> hechosExistentes.stream().noneMatch(hExistente ->
                        Objects.equals(hExistente.getTitulo(), hNuevo.getTitulo()) &&
                                Objects.equals(hExistente.getDescripcion(), hNuevo.getDescripcion()) &&
                                Objects.equals(hExistente.getFuenteOrigen(), hNuevo.getFuenteOrigen())
                ))
                .toList();
    }

    @Override
    public void consensuarHechos() {
        List<Coleccion> colecciones =  this.coleccionRepository.findAll();
        System.out.println("las colecciones son" + colecciones);
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

    public List<HechoOutputDTO> obtenerTodosLosHechos(Map<String, String> filtros){
        List<Hecho> hechos = hechoRepository.findAllWithRelations();

        if (filtros.containsKey("fuente")) {
            String puertoFuente = filtros.get("fuente");
            Fuente fuente = this.fuenteRepository.findByPuerto(puertoFuente);
            if (fuente != null ) {
                hechos = hechos.stream().filter(h-> h.getFuenteOrigen().equals(fuente.getOrigenHechos())).collect(Collectors.toList());
            }
        }

        List<CriterioDePertenencia> criterios = CriterioDePertenenciaFactory.crearCriterios(filtros);

        hechos = hechos.stream()
                .filter(hecho -> criterios.isEmpty() ||
                        criterios.stream().allMatch(c -> c.cumpleCriterio(hecho)))
                .collect(Collectors.toList());

        return hechosToDTO(hechos);
    }

    public List<StatDTO> getProvinciaMasReportadaPorTodasLasColecciones() {
        List<StatDTO> stats = this.hechoRepository.countHechosByProvinciaAndColeccion();
        stats.forEach(s -> {
            s.setDescripcion(DescripcionStat.hechos_provincia_coleccion);
            s.setFechaStat(LocalDateTime.now());
        });
        return stats;
    }

    public List<StatDTO> getCategoriaMasReportada(){
        List<StatDTO> stats = hechoRepository.findCategoriaWithMostHechos();
        stats.forEach(s -> {
            s.setDescripcion(DescripcionStat.hechos_categoria);
            s.setFechaStat(LocalDateTime.now());
        });
        return stats;
    }

    public List<StatDTO> getHoraMasReportada(){
        List<StatDTO> stats = hechoRepository.findHoraWithMostHechosByCategoria();
        stats.forEach(s -> {
            s.setDescripcion(DescripcionStat.hechos_categoria_hora);
            s.setFechaStat(LocalDateTime.now());
        });
        return stats;
    }

    public List<StatDTO> getProviniciaMasReportadaPorCategoria(){
        List<StatDTO> stats = hechoRepository.findProvinciaWithMostHechosByCategoria();
        stats.forEach(s -> {
            s.setDescripcion(DescripcionStat.hechos_categoria_provincia);
            s.setFechaStat(LocalDateTime.now());
        });
        return stats;
    }


    @Transactional
    public void sincronizarHechos(List<Hecho> hechosNuevos, Coleccion coleccion) {
        System.out.println("--- Iniciando proceso de sincronización de Hechos ---");

        if (hechosNuevos == null || hechosNuevos.isEmpty()) {
            System.out.println("No hay hechos nuevos para sincronizar.");
            return;
        }

        List<Hecho> hechosEnDB = hechoRepository.findAll();

        Function<Hecho, String> compositeKey = h -> (h.getTitulo() + "|||" + h.getDescripcion()).toLowerCase();
        Map<String, Hecho> mapaHechosEnDB = hechosEnDB.stream()
                .collect(Collectors.toMap(
                        compositeKey,
                        Function.identity(),
                        (h1, h2) -> h1
                ));

        List<Hecho> hechosParaGuardar = new ArrayList<>();

        int actualizados = 0;
        int insertados = 0;

        for (Hecho hechoNuevo : hechosNuevos) {
            String key = compositeKey.apply(hechoNuevo);
            Hecho hechoExistente = mapaHechosEnDB.get(key);

            if (hechoExistente != null) {
                System.out.println("Actualizando hecho existente con título: " + hechoNuevo.getTitulo());


                hechoExistente.setEsValido(hechoNuevo.getEsValido());
                hechoExistente.setFechaHecho(hechoNuevo.getFechaHecho());
                hechoExistente.setUbicacion(hechoNuevo.getUbicacion()); // Asumiendo Cascade.MERGE
                hechoExistente.setCategoria(hechoNuevo.getCategoria()); // Asumiendo Cascade.MERGE

                hechosParaGuardar.add(hechoExistente);
                actualizados++;

            } else {
                System.out.println("Añadiendo nuevo hecho con título: " + hechoNuevo.getTitulo());
                hechosParaGuardar.add(hechoNuevo);
                insertados++;
            }
        }

    if (!hechosParaGuardar.isEmpty()) {
            hechoRepository.saveAll(hechosParaGuardar);
            hechosParaGuardar.forEach(h-> h.addColeccion(coleccion));
        }

        System.out.println("--- Sincronización finalizada ---");
        System.out.println("Hechos actualizados: " + actualizados);
        System.out.println("Hechos nuevos insertados: " + insertados);
    }

    public List<Fuente> getFuentes(){
        return this.fuenteRepository.findAll();
    }

    public List<CriterioDePertenenciaDTO> getCriterios(){
        List<CriterioDePertenencia> criterios = this.criterioRepository.findAll();

        return criterios.stream().map(ColeccionUtil::criterioToDTO).collect(Collectors.toList());
    }

    public HechoOutputDTO obtenerHechoPorId(Long id){
        Hecho hecho = hechoRepository.findById(id).orElse(null);
        if (hecho == null){
            return null;
        }
        return HechoUtil.hechoToDTO(hecho);
    }
}
