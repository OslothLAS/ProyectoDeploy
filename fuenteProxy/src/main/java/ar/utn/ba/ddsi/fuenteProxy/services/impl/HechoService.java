package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.dtos.AuthRequest;
import ar.utn.ba.ddsi.fuenteProxy.dtos.AuthResponse;
import ar.utn.ba.ddsi.fuenteProxy.dtos.DesastresResponse;
import ar.utn.ba.ddsi.fuenteProxy.dtos.HechoDto;
import ar.utn.ba.ddsi.fuenteProxy.mappers.DatosHechosMapper;
import ar.utn.ba.ddsi.fuenteProxy.repository.IHechoRepository;
import ar.utn.ba.ddsi.fuenteProxy.services.IHechoService;
import entities.hechos.DatosHechos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

        // Obtener la primera página de hechos desde el campo 'data'
        DesastresResponse response = webClient.get()
                .uri(desastresPath)  // ejemplo: "/public/api/desastres?page=1"
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(DesastresResponse.class)
                .block();

        List<HechoDto> hechosDto = response != null ? response.getData() : List.of();
/*
        List<DatosHechos> hechos = hechosDto.stream()
                .map(DatosHechosMapper::map)
                .collect(Collectors.toList());
*/
        hechosDto.forEach(hechoRepository::save);

        return hechosDto;
    }
}
