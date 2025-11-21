package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudesInputDto;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.Metamapa;
import ar.utn.ba.ddsi.fuenteProxy.models.repositories.IRepositoryMetamapa;
import ar.utn.ba.ddsi.fuenteProxy.services.IHechoService;
import ar.utn.ba.ddsi.fuenteProxy.services.ISolicitudService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudService implements ISolicitudService {

    private final WebClient webClient;
    private final IRepositoryMetamapa metamapaRepository;
    private final IHechoService hechoService;

    private static final String SOLICITUDES_PATH = "/api/solicitudes";

    public SolicitudService(IHechoService hechoService, IRepositoryMetamapa metamapaRepository) {
        this.hechoService = hechoService;
        this.metamapaRepository = metamapaRepository;
        this.webClient = WebClient.builder().build();
    }

    @Override
    public List<SolicitudDto> getSolicitudesXmetamapa(Long metamapaId) {
        Metamapa metamapa = metamapaRepository.findById(metamapaId);
        if (metamapa == null) return List.of();

        String fullUrl = metamapa.getUrl() + SOLICITUDES_PATH;
        return fetchSolicitudesFromUrl(fullUrl);
    }

    @Override
    public SolicitudDto postSolicitudesXmetamapa(Long metamapaId, SolicitudesInputDto solicitud) {
        Metamapa metamapa = metamapaRepository.findById(metamapaId);
        if (metamapa == null) {
            throw new IllegalArgumentException("Metamapa no encontrado con id: " + metamapaId);
        }

        // Verificar si el hecho existe dentro del metamapa
        boolean hechoExiste = hechoService.getHechosXmetamapa(metamapaId).stream()
                .anyMatch(hecho -> hecho.getId().equals(solicitud.getId_hecho()));

        if (!hechoExiste) {
            throw new IllegalArgumentException("El hecho con ID " + solicitud.getId_hecho()
                    + " no existe en el metamapa " + metamapaId);
        }

        SolicitudDto solicitudDto = convertirInputADto(solicitud);

        try {
            String fullUrl = metamapa.getUrl() + SOLICITUDES_PATH;

            SolicitudDto solicitudDto1 = webClient.post()
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

    private SolicitudDto convertirInputADto(SolicitudesInputDto input) {
        SolicitudDto dto = new SolicitudDto();
        dto.setId_solicitante(input.getId_solicitante());
        dto.setId_hecho(input.getId_hecho());
        dto.setJustificacion(input.getJustificacion());
        dto.setEstadoSolicitudEliminacion("Pendiente");
        dto.setFechaDeCreacion(LocalDateTime.now().toString());
        dto.setHistorialDeSolicitud(List.of("Pendiente"));
        return dto;
    }
}
