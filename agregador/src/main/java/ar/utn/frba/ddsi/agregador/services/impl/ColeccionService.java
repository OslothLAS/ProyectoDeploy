package ar.utn.frba.ddsi.agregador.services.impl;

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.utn.frba.ddsi.agregador.utils.ColeccionUtil.dtoToColeccion;
import static ar.utn.frba.ddsi.agregador.utils.ColeccionUtil.fuenteDTOtoFuente;
import static ar.utn.frba.ddsi.agregador.utils.HechoUtil.hechosToDTO;
import static ar.utn.frba.ddsi.agregador.utils.HechoUtil.filtrarHechosRepetidos;
import static ar.utn.frba.ddsi.agregador.utils.HechoUtil.obtenerHechosValidos;
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

    @Autowired
    @Qualifier("hechoWebClient")
    private WebClient hechoWebClient;

    public ColeccionService(IHechoRepository hechoRepository, IColeccionRepository coleccionRepository,
                            ICategoriaRepository categoriaRepository, IUbicacionRepository ubicacionRepository,
                            IProvinciaRepository provinciaRepository, ILocalidadRepository localidadRepository,
                            IFuenteRepository fuenteRepository, ICriterioRepository criterioRepository) {
        this.hechoRepository = hechoRepository;
        this.coleccionRepository = coleccionRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.provinciaRepository = provinciaRepository;
        this.localidadRepository = localidadRepository;
        this.fuenteRepository = fuenteRepository;
        this.criterioRepository = criterioRepository;
    }


    public Categoria obtenerOCrearCategoria(String nombre) {
        String clave = normalizarTrimTexto(nombre);
        return categoriaRepository.findByCategoriaNormalizada(clave)
                .orElseGet(() -> categoriaRepository.save(new Categoria(nombre)));
    }


    public List<Coleccion> getColeccionesClass(){
        return this.coleccionRepository.findAll();
   }

    @Transactional
    public void createColeccion(ColeccionInputDTO coleccionDTO) {
        List<String> puertos = coleccionDTO.getFuentes().stream().map(Fuente::getPuerto).toList();

        List<Fuente> fuentesExistentes = fuenteRepository.findAllByPuertoIn(puertos);

        Map<String, Fuente> fuentesMap = fuentesExistentes.stream()
                .collect(Collectors.toMap(Fuente::getPuerto, f -> f));

        List<Fuente> fuentesFinales = coleccionDTO.getFuentes().stream()
                .map(fuenteDTO -> {
                    if (fuentesMap.containsKey(fuenteDTO.getPuerto())) {
                        return fuentesMap.get(fuenteDTO.getPuerto());
                    }
                    return fuenteRepository.save(fuenteDTO);
                })
                .toList();

        List<CriterioDePertenencia> criterios = this.obtenerCriterios(coleccionDTO.getCriterios());

        Coleccion nuevaColeccion = dtoToColeccion(coleccionDTO, fuentesFinales);
        nuevaColeccion.setCriteriosDePertenencia(criterios);

        List<Coleccion> coleccionesExistentes = coleccionRepository.findAll();

        if (coleccionesExistentes.contains(nuevaColeccion)) {
            throw new IllegalArgumentException("La coleccion ya existe");
        }

        this.coleccionRepository.save(nuevaColeccion);

        List<Hecho> hechos = this.procesarHechos(fuentesFinales, criterios, nuevaColeccion);

       this.sincronizarHechos(hechos, nuevaColeccion);
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
        /*.stream()
                .peek(h -> h.addColeccion(coleccion))
                .collect(Collectors.toList());*/
    }

    public void normalizarHechos(List<Hecho> hechos){
        Map<String, Ubicacion> ubicacionesPorClave = new HashMap<>();

        hechos.parallelStream().forEach(Hecho::normalizarHecho);

        Set<String> clavesUbicacion = new HashSet<>();
        for (Hecho hecho : hechos) {
            if (hecho.getUbicacion() != null) {
                Ubicacion ub = hecho.getUbicacion();
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
                for (Ubicacion ub : nuevasUbicaciones) {
                    if (ub.getLocalidad() != null) {
                        Localidad loc = ub.getLocalidad();
                        Provincia prov = loc.getProvincia();

                        if (prov != null) {
                            String nombreProvincia = prov.getNombre();
                            Provincia provinciaExistente = provinciaRepository.findByNombre(nombreProvincia)
                                    .orElseGet(() -> provinciaRepository.save(new Provincia(nombreProvincia)));
                            loc.setProvincia(provinciaExistente);
                        }

                        // Buscar localidad existente (por nombre y provincia)
                        String nombreLocalidad = loc.getNombre();
                        Localidad localidadExistente = localidadRepository
                                .findByNombreAndProvinciaNombre(nombreLocalidad, loc.getProvincia().getNombre())
                                .orElseGet(() -> localidadRepository.save(new Localidad(loc.getProvincia(), nombreLocalidad)));

                        ub.setLocalidad(localidadExistente);
                    }
                }
                List<Ubicacion> guardadas = ubicacionRepository.saveAll(nuevasUbicaciones);

                for (Ubicacion guardada : guardadas) {
                    String clave = guardada.getLatitud() + "," + guardada.getLongitud();
                    ubicacionesPorClave.put(clave, guardada);
                }
            }
        }

        for (Hecho hecho : hechos) {
            if (hecho.getUbicacion() != null) {
                Ubicacion ub = hecho.getUbicacion();
                String clave = ub.getLatitud() + "," + ub.getLongitud();
                hecho.setUbicacion(ubicacionesPorClave.get(clave));
            }

            String nombreCategoria = hecho.getCategoria().getCategoria();
            Categoria categoriaExistente = obtenerOCrearCategoria(nombreCategoria);
            hecho.setCategoria(categoriaExistente);
        }
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
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idColeccion));

        List<Hecho> hechosDeColeccion = tomarHechosDeColeccion(coleccion);
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


    @Transactional
    @Override
    public void actualizarHechos(){
        List<Coleccion> colecciones = this.coleccionRepository.findAll();

        colecciones.forEach(coleccion -> {
            List<CriterioDePertenencia> criterios = Optional.ofNullable(coleccion.getCriteriosDePertenencia())
                    .orElse(List.of());
          
            List<Hecho> hechosDeFuentes = tomarHechosFuentes(coleccion.getImportadores(), criterios);
            List<Hecho> hechosRepository = hechoRepository.findAll();
            List<Hecho> hechosSinRepetir = obtenerHechosValidos(filtrarHechosRepetidos(hechosRepository, hechosDeFuentes));
            this.normalizarHechos(hechosDeFuentes);

            hechoRepository.saveAll(hechosSinRepetir);
            asignarColeccionAHechos(hechosSinRepetir, coleccion);
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

    public List<HechoOutputDTO> obtenerTodosLosHechos(Map<String, String> filtros){
        List<Hecho> hechos = hechoRepository.findAll();

        if (filtros.containsKey("fuente")) {
            String puertoFuente = filtros.get("fuente");
            Fuente fuente = this.fuenteRepository.findByPuerto(puertoFuente);
            if (fuente != null ) {
                hechos = hechos.stream().filter(h-> h.getFuenteOrigen().equals(fuente.getOrigenHechos())).collect(Collectors.toList());
            }
        }

        List<CriterioDePertenencia> criterios = CriterioDePertenenciaFactory.crearCriterios(filtros);

        hechos = hechos.stream()
                .filter(Hecho::getEsValido)
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
                .collect(Collectors.toMap(compositeKey, Function.identity()));

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
}
