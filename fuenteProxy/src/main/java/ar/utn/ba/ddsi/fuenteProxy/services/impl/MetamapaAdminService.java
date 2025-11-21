package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionInputDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.hecho.HechoOutputDTO;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudesInputDto;
import ar.utn.ba.ddsi.fuenteProxy.mappers.HechoUtil;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.Metamapa;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Handle;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteProxy.models.repositories.IRepositoryMetamapa;
import ar.utn.ba.ddsi.fuenteProxy.services.IMetamapaAdminService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ar.utn.ba.ddsi.fuenteProxy.utils.NormalizadorTexto.normalizarTrimTexto;

@Service
public class MetamapaAdminService implements IMetamapaAdminService {

    private final WebClient webClient;
    private final IRepositoryMetamapa metamapaRepository;

    private static final String HECHOS_PATH = "/api/hechos";
    private static final String SOLICITUDES_PATH = "/api/solicitudes";
    private static final String COLECCIONES_PATH = "/api/colecciones";

    public MetamapaAdminService(IRepositoryMetamapa metamapaRepository) {
        this.metamapaRepository = metamapaRepository;
        this.webClient = WebClient.builder().build();
    }

    @Override
    public List<Hecho> getHechos() {
        return metamapaRepository.findAll().stream()
                .flatMap(metamapa -> {
                    String fullUrl = metamapa.getUrl() + HECHOS_PATH;

                    List<Hecho> hechos = fetchHechosFromUrl(fullUrl);

                    return hechos.stream();
                })
                .toList();
    }

    @Override
    public List<Hecho> getHechosXcategoria(String categoria) {
        return getHechos().stream()
                .filter(hecho -> hecho.getCategoria() != null)
                .filter(hecho -> hecho.getCategoria().getCategoriaNormalizada().equalsIgnoreCase(normalizarTrimTexto(categoria)))
                .toList();
    }

    @Override
    public List<Hecho> getHechosXmetamapa(Long id) {
        Metamapa metamapa = metamapaRepository.findById(id);
        String fullUrl = metamapa.getUrl() + HECHOS_PATH;
        return fetchHechosFromUrl(fullUrl);
    }

    @Override
    public List<Hecho> getHechosXcoleccionXmetamapa(Handle id_coleccion, Long metamapa) {
        return getHechosXmetamapa(metamapa).stream()
                .filter(hecho -> hecho.getHandles() != null)
                .filter(hecho -> hecho.getHandles().contains(id_coleccion))
                .toList();
    }

    @Override
    public List<Hecho> getHechosXColeccionXmetampaXModoNavegacion(String modoNavegacion, Handle id_coleccion, Long metamapa) {
        List<Hecho> hechosColeccionMetamapa = getHechosXcoleccionXmetamapa(id_coleccion, metamapa);

        if (modoNavegacion.equals("irrestricta")) {
            return hechosColeccionMetamapa;
        } else if (modoNavegacion.equals("curada")) {
            return hechosColeccionMetamapa.stream()
                    .filter(Hecho::getEsConsensuado)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // O lanzar una excepción si no es un modo válido
        }
    }

    @Override
    public List<SolicitudDto> getSolicitudesXmetamapa(Long metamapaId) {
        Metamapa metamapa = metamapaRepository.findById(metamapaId);
        if (metamapa == null) return List.of();

        String fullUrl = metamapa.getUrl() + SOLICITUDES_PATH;
        return fetchSolicitudesFromUrl(fullUrl);
    }

    @Override
    public List<ColeccionDto> getColeccionesXmetamapa(Long metamapaId) {
        Metamapa metamapa = metamapaRepository.findById(metamapaId);
        if (metamapa == null) return List.of();
        String fullUrl = metamapa.getUrl() + COLECCIONES_PATH;
        return fetchColeccionesFromUrl(fullUrl);
    }

    @Override
    public ColeccionDto postColeccionesXmetamapa(Long metamapaId, ColeccionInputDto inputDto) {
        Metamapa metamapa = metamapaRepository.findById(metamapaId);
        if (metamapa == null) {
            throw new IllegalArgumentException("Metamapa no encontrado con id: " + metamapaId);
        }

        try {
            String fullUrl = metamapa.getUrl() + COLECCIONES_PATH;

            ColeccionDto nuevaColeccion = this.convertirColeccionInputADto(inputDto);

            ColeccionDto coleccionCreada = webClient.post()
                    .uri(URI.create(fullUrl))
                    .bodyValue(nuevaColeccion)
                    .retrieve()
                    .bodyToMono(ColeccionDto.class)
                    .block();

            return nuevaColeccion;

        } catch (Exception e) {
            System.err.println("Error al postear la coleccion: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ColeccionDto putColeccionesXmetamapa(Long metamapaID, Long id_coleccion, ColeccionInputDto inputColeccion){
        Metamapa metamapa = metamapaRepository.findById(metamapaID);
        if (metamapa == null) {
            throw new IllegalArgumentException("Metamapa no encontrado con id: " + metamapaID);
        }

        List<ColeccionDto> coleccionDtos = this.getColeccionesXmetamapa(metamapaID);
        if(coleccionDtos.isEmpty()) {
            throw new IllegalArgumentException("EL metamapa no contiene colecciones");
        }

        ColeccionDto coleccionEncontrada = null;

        for (ColeccionDto coleccionDto : coleccionDtos) {
            if (coleccionDto.getId().equals(id_coleccion)) {
                coleccionEncontrada = coleccionDto;
                break;
            }
        }

        if (coleccionEncontrada != null) {
            ColeccionDto nuevaColeccion = this.convertirColeccionInputADto(inputColeccion);
            try {
                String fullUrl = metamapa.getUrl() + COLECCIONES_PATH;

                ColeccionDto coleccionCreada = webClient.put()
                        .uri(URI.create(fullUrl))
                        .bodyValue(nuevaColeccion)
                        .retrieve()
                        .bodyToMono(ColeccionDto.class)
                        .block();

                return nuevaColeccion;

            } catch (Exception e) {
                System.err.println("Error al actualizar la coleccion: " + e.getMessage());
                return null;
            }
        }
        else throw new IllegalArgumentException("Coleccion no encontrado con id: " + id_coleccion);



    }

    @Override
    public void eliminarColeccionesXmetamapa(Long metamapaID, Long id_coleccion){
        Metamapa metamapa = metamapaRepository.findById(metamapaID);
        if (metamapa == null) {
            throw new IllegalArgumentException("Metamapa no encontrado con id: " + metamapaID);
        }

        List<ColeccionDto> coleccionDtos = this.getColeccionesXmetamapa(metamapaID);
        if(coleccionDtos.isEmpty()) {
            throw new IllegalArgumentException("EL metamapa no contiene colecciones");
        }

        ColeccionDto coleccionEncontrada = null;

        for (ColeccionDto coleccionDto : coleccionDtos) {
            if (coleccionDto.getId().equals(id_coleccion)) {
                coleccionEncontrada = coleccionDto;
                break;
            }
        }

        if (coleccionEncontrada != null) {

            try {
                String fullUrl = metamapa.getUrl() + COLECCIONES_PATH + "/" + id_coleccion;

                webClient.delete()
                        .uri(URI.create(fullUrl))
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();


            } catch (Exception e) {
                System.err.println("Error al actualizar la coleccion: " + e.getMessage());
            }
        }
        else throw new IllegalArgumentException("Coleccion no encontrado con id: " + id_coleccion);



    }

    private ColeccionDto convertirColeccionInputADto(ColeccionInputDto inputDto) {
        ColeccionDto dto = new ColeccionDto();
        dto.setTitulo(inputDto.getTitulo());
        dto.setDescripcion(inputDto.getDescripcion());
        dto.setCriteriosDePertenencia(inputDto.getCriteriosDePertenencia());
        dto.setFuente("Externa");
        dto.setFechaYHoraDeActualizacion(LocalDateTime.now().toString());

        return dto;
    }

    @Override
    public SolicitudDto postSolicitudesXmetamapa(Long metamapaId, SolicitudesInputDto solicitud) {
        Metamapa metamapa = metamapaRepository.findById(metamapaId);
        if (metamapa == null) {
            throw new IllegalArgumentException("Metamapa no encontrado con id: " + metamapaId);
        }

        // Verificar si el hecho existe dentro de ese metamapa
        boolean hechoExiste = getHechosXmetamapa(metamapaId).stream()
                .anyMatch(hecho -> hecho.getId().equals(solicitud.getId_hecho()));

        if (!hechoExiste) {
            throw new IllegalArgumentException("El hecho con ID " + solicitud.getId_hecho()
                    + " no existe en el metamapa " + metamapaId);
        }

        SolicitudDto solicitudDto=convertirInputADto(solicitud);

        try {
            String fullUrl = metamapa.getUrl() + SOLICITUDES_PATH;

            SolicitudDto solicitudCreada = webClient.post()
                    .uri(URI.create(fullUrl))
                    .bodyValue(solicitudDto)
                    .retrieve()
                    .bodyToMono(SolicitudDto.class)
                    .block();

            return solicitudDto;

        } catch (Exception e) {
            System.err.println("Error al postear la solicitud: " + e.getMessage());
            return null;
        }
    }


    @Override
    public SolicitudDto putSolicitudesXmetamapa(Long metamapaId, Long id_solicitud, String estado) {
        Metamapa metamapa = metamapaRepository.findById(metamapaId);
        if (metamapa == null) {
            throw new IllegalArgumentException("Metamapa no encontrado con id: " + metamapaId);
        }

        //Verificar que existe la solicitud
        List<SolicitudDto> solicitudDtoList = this.getSolicitudesXmetamapa(metamapaId);
        if(solicitudDtoList.isEmpty()) {
            throw new IllegalArgumentException("EL metamapa no contiene a esta solicitud: " + id_solicitud);
        }

        SolicitudDto solicitudEncontrada = null;

        for (SolicitudDto solicitudDto : solicitudDtoList) {
            if (solicitudDto.getId().equals(id_solicitud)) {
                solicitudEncontrada = solicitudDto;
                break;
            }
        }

        if (solicitudEncontrada != null) {

            solicitudEncontrada.setEstadoSolicitudEliminacion(estado);
            solicitudEncontrada.agregarEstadoHistorial(estado);

            try {
                String fullUrl = metamapa.getUrl() + SOLICITUDES_PATH + "/" + id_solicitud;

                SolicitudDto solicitudActualizada = webClient.put()
                        .uri(URI.create(fullUrl))
                        .bodyValue(solicitudEncontrada)
                        .retrieve()
                        .bodyToMono(SolicitudDto.class)
                        .block();
                return solicitudEncontrada;

            } catch (Exception e) {
                System.err.println("Error al actualizar la solicitud: " + e.getMessage());
                return null;
            }
        }
        else throw new IllegalArgumentException("Solicitud no encontrado con id: " + id_solicitud);


    }


    private List<ColeccionDto> fetchColeccionesFromUrl(String url) {
        try {
            List<ColeccionDto> colecciones = webClient.get()
                    .uri(URI.create(url))
                    .retrieve()
                    .bodyToFlux(ColeccionDto.class)
                    .collectList()
                    .block();

            return colecciones != null ? colecciones : List.of();
        } catch (Exception e) {
            System.err.println("Error al obtener colecciones desde: " + url + " - " + e.getMessage());
            return List.of();
        }
    }


    private List<SolicitudDto> fetchSolicitudesFromUrl(String url) {
        try {
            List<SolicitudDto> solicitudesDto = webClient.get()
                    .uri(URI.create(url))
                    .retrieve()
                    .bodyToFlux(SolicitudDto.class)
                    .collectList()
                    .block();
            return solicitudesDto != null ? solicitudesDto : List.of();
        } catch (Exception e) {
            System.err.println("Error al obtener solicitudes desde: " + url + " - " + e.getMessage());
            return List.of();
        }
    }

    private List<Hecho> fetchHechosFromUrl(String url) {
        try {
            List<HechoOutputDTO> hechosDto = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(HechoOutputDTO.class)
                    .collectList()
                    .block();
            if (hechosDto == null) return List.of();
            return hechosDto.stream()
                    .map(HechoUtil::hechoDTOtoHecho)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    public SolicitudDto convertirInputADto(SolicitudesInputDto input) {
        SolicitudDto dto = new SolicitudDto();
        dto.setId_solicitante(input.getId_solicitante());
        dto.setId_hecho(input.getId_hecho());
        dto.setJustificacion(input.getJustificacion());

        // Podés inicializar otros campos si querés valores por defecto
        dto.setEstadoSolicitudEliminacion("Pendiente");
        dto.setFechaDeCreacion(LocalDateTime.now().toString());
        dto.setHistorialDeSolicitud(List.of("Pendiente"));

        return dto;
    }
}