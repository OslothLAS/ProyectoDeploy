package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.dtos.api.AuthRequest;
import ar.utn.ba.ddsi.fuenteProxy.dtos.api.AuthResponse;
import ar.utn.ba.ddsi.fuenteProxy.dtos.api.DesastresResponse;
import ar.utn.ba.ddsi.fuenteProxy.dtos.hecho.HechoDto;
import ar.utn.ba.ddsi.fuenteProxy.mappers.HechoMapper;
import ar.utn.ba.ddsi.fuenteProxy.services.IApiService;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.factories.CriterioDePertenenciaFactory;
import entities.hechos.Hecho;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiService implements IApiService {

    private final WebClient webClient;
    private final WebClient authClient;

    private final String baseUrl;
    private final String loginPath;
    private final String desastresPath;
    private final String email;
    private final String password;

    private String token;

    public ApiService(
            @Value("${desastre.api.base-url}") String baseUrl,
            @Value("${desastre.api.login-path}") String loginPath,
            @Value("${desastre.api.desastres-path}") String desastresPath,
            @Value("${desastre.api.email}") String email,
            @Value("${desastre.api.password}") String password
    ) {
        this.baseUrl = baseUrl;
        this.loginPath = loginPath;
        this.desastresPath = desastresPath;
        this.email = email;
        this.password = password;

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
    public List<Hecho> getHechos(Map<String, String> filtros) {
        if (token == null) {
            token = obtenerToken();
        }

        DesastresResponse response = webClient.get()
                .uri(desastresPath)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(DesastresResponse.class)
                .block();

        List<HechoDto> hechosDto = response != null ? response.getData() : List.of();

        // Mapear los HechoDto a Hecho
        List<Hecho> hechos = hechosDto.stream()
                .map(HechoMapper::mapHechoDtoToHecho)
                .toList();

        List<CriterioDePertenencia> criterios = CriterioDePertenenciaFactory.crearCriterios(filtros);

        if (criterios.isEmpty()) {
            return hechos;
        }

        return hechos.stream()
                //.filter(Hecho::getEsValido) hay que ver si el hecho de otra instancia siempre es valido o no (imagino q si)
                .filter(hecho -> criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho)))
                .collect(Collectors.toList());
    }
}
