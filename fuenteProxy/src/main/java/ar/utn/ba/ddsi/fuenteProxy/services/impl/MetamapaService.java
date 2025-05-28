package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.dtos.AuthRequest;
import ar.utn.ba.ddsi.fuenteProxy.dtos.AuthResponse;
import ar.utn.ba.ddsi.fuenteProxy.dtos.DesastresResponse;
import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;
import ar.utn.ba.ddsi.fuenteProxy.mappers.HechoMapper;
import ar.utn.ba.ddsi.fuenteProxy.repositories.IRepositoryMetamapa;
import ar.utn.ba.ddsi.fuenteProxy.services.IMetamapaService;
import entities.hechos.Hecho;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.List;

@Service
public class MetamapaService implements IMetamapaService {

    private final WebClient webClient;
    private final IRepositoryMetamapa metamapaRepository;

    private static final String HECHOS_PATH = "/api/hechos";

    public MetamapaService(IRepositoryMetamapa metamapaRepository) {
        this.metamapaRepository = metamapaRepository;
        this.webClient = WebClient.builder().build();
    }

    @Override
    public List<Hecho> getHechos() {
        return metamapaRepository.findAll().stream()
                .flatMap(metamapa -> {
                    String fullUrl = metamapa.getUrl() + HECHOS_PATH;

                    List<HechoDto> hechosDto;
                    try {
                        hechosDto = webClient.get()
                                .uri(fullUrl)
                                .retrieve()
                                .bodyToFlux(HechoDto.class)
                                .collectList()
                                .block();
                    } catch (Exception e) {
                        // Si falla una URL, devolvemos una lista vacía y seguimos
                        hechosDto = List.of();
                    }

                    return hechosDto.stream()
                            .map(HechoMapper::mapHechoDtoToHecho);
                })
                .toList();
    }
}


