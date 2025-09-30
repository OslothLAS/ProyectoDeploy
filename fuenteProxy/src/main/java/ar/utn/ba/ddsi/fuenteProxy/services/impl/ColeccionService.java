package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionDto;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.Metamapa;
import ar.utn.ba.ddsi.fuenteProxy.models.repositories.IRepositoryMetamapa;
import ar.utn.ba.ddsi.fuenteProxy.services.IColeccionService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;
import java.util.List;

@Service
public class ColeccionService implements IColeccionService {
    private final WebClient webClient;
    private final IRepositoryMetamapa metamapaRepository;

    private static final String COLECCIONES_PATH = "/api/colecciones";


    public ColeccionService(IRepositoryMetamapa metamapaRepository) {
        this.metamapaRepository = metamapaRepository;
        this.webClient = WebClient.builder().build();
    }

    @Override
    public List<ColeccionDto> getColeccionesXmetamapa(Long metamapaId) {
        Metamapa metamapa = metamapaRepository.findById(metamapaId);
        if (metamapa == null) return List.of();
        String fullUrl = metamapa.getUrl() + COLECCIONES_PATH;
        return fetchColeccionesFromUrl(fullUrl);
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


}
