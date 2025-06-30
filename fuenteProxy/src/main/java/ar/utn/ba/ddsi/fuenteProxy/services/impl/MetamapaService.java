package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.dtos.ColeccionDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.SolicitudDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.SolicitudesInputDto;
import ar.utn.ba.ddsi.fuenteProxy.mappers.HechoMapper;
import ar.utn.ba.ddsi.fuenteProxy.repositories.IRepositoryMetamapa;
import ar.utn.ba.ddsi.fuenteProxy.services.IMetamapaService;
import entities.Metamapa;
import entities.colecciones.Handle;
import entities.hechos.Hecho;
import entities.solicitudes.SolicitudEliminacion;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetamapaService implements IMetamapaService {

    private final WebClient webClient;
    private final IRepositoryMetamapa metamapaRepository;

    private static final String HECHOS_PATH = "/api/hechos";
    private static final String SOLICITUDES_PATH = "/api/solicitudes";
    private static final String COLECCIONES_PATH = "/api/colecciones";

    public MetamapaService(IRepositoryMetamapa metamapaRepository) {
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
                 .filter(hecho -> hecho.getDatosHechos() != null)
                 .filter(hecho -> hecho.getDatosHechos().getCategoria() != null)
                 .filter(hecho -> hecho.getDatosHechos().getCategoria().equalsIgnoreCase(categoria.trim()))
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
                .filter(hecho -> hecho.getDatosHechos() != null)
                .filter(hecho -> hecho.getColecciones() != null)
                .filter(hecho -> hecho.getColecciones().contains(id_coleccion))
                .toList();
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

            return solicitudDto; //? solicitudCreada.getId() : null;

        } catch (Exception e) {
            System.err.println("Error al postear la solicitud: " + e.getMessage());
            return null;
        }
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
            List<HechoDto> hechosDto = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(HechoDto.class)
                    .collectList()
                    .block();
            if (hechosDto == null) return List.of();
            return hechosDto.stream()
                    .map(HechoMapper::mapHechoDtoToHecho)
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




