package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.dtos.AuthRequest;
import ar.utn.ba.ddsi.fuenteProxy.dtos.AuthResponse;
import ar.utn.ba.ddsi.fuenteProxy.dtos.DesastresResponse;
import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;
import ar.utn.ba.ddsi.fuenteProxy.mappers.HechoMapper;
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

    private final String baseUrl;
    private final String desastresPath;

    {
        this.baseUrl = "https://215bc932-75a4-4afe-8a6d-87e7753f3f94.mock.pstmn.io";
        this.desastresPath = "api/hechos";


        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }


    @Override
    public List<Hecho> getHechos() {

        List<HechoDto> hechosDto = webClient.get()
                .uri(desastresPath)
                .retrieve()
                .bodyToFlux(HechoDto.class)
                .collectList()
                .block();

        // Mapear los HechoDto a Hecho
        List<Hecho> hechos = hechosDto.stream()
                .map(HechoMapper::mapHechoDtoToHecho)
                .toList(); // Java 16+ (si estás en <16, usá collect(Collectors.toList()))


        return hechos;
    }
}
