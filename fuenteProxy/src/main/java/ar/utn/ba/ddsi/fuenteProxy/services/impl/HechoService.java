package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.dtos.AuthRequest;
import ar.utn.ba.ddsi.fuenteProxy.dtos.AuthResponse;
import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;
import entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteProxy.mappers.HechoMapper;
import ar.utn.ba.ddsi.fuenteProxy.repository.IHechoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ar.utn.ba.ddsi.fuenteProxy.services.IHechoService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechoService implements IHechoService {

    private final WebClient webClient;
    private final WebClient authClient;
    private final IHechoRepository hechoRepository;

    private final String baseUrl;
    private final String loginPath;
    private final String desastresPath;
    private final String email;
    private final String password;

    private String token;

    public HechoService(
            @Value("${desastre.api.base-url}") String baseUrl,
            @Value("${desastre.api.login-path}") String loginPath,
            @Value("${desastre.api.desastres-path}") String desastresPath,
            @Value("${desastre.api.email}") String email,
            @Value("${desastre.api.password}") String password,
            IHechoRepository hechoRepository
    ) {
        this.baseUrl = baseUrl;
        this.loginPath = loginPath;
        this.desastresPath = desastresPath;
        this.email = email;
        this.password = password;
        this.hechoRepository = hechoRepository;

        this.authClient = WebClient.builder().baseUrl(baseUrl).build();
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    private String obtenerToken() {
        AuthRequest request = new AuthRequest(email, password);
        Mono<AuthResponse> responseMono = authClient.post()
                .uri(loginPath)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class);

        AuthResponse response = responseMono.block();
        return response != null ? response.getToken() : null;
    }

    @Override
    public List<HechoDto> getHechos() {
        if (token == null) {
            token = obtenerToken();
        }

        List<HechoDto> hechosDto = webClient.get()
                .uri(desastresPath)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(HechoDto.class)
                .collectList()
                .block();

        List<Hecho> hechos = hechosDto.stream()
                .map(HechoMapper::map)
                .collect(Collectors.toList());

        hechos.forEach(hechoRepository::save);

        return hechosDto;
    }
}
