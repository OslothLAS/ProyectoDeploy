package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoInputEditarApi;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.HechoApiInputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoApiOutputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoAGREInput;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.internal.WebApiCallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
public class HechosApiService {
    private static final Logger log = LoggerFactory.getLogger(HechosApiService.class);

    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String hechosServiceUrl;
    private final String hechoTotalesServiceUrl;
    private final String hechosStaticServiceUrl;


    @Autowired
    public HechosApiService(WebApiCallerService webApiCallerService,
                            @Value("${hechos.service.url}") String hechosServiceUrl,
                            @Value("${hechos.totales.service.url}") String hechoTotalesServiceUrl,
                            @Value("${hechos.static.service.url}") String hechosStaticServiceUrl)
                            {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.hechosServiceUrl = hechosServiceUrl;
        this.hechoTotalesServiceUrl = hechoTotalesServiceUrl;
        this.hechosStaticServiceUrl = hechosStaticServiceUrl;
    }

    // Crear un hecho
    public void createHecho(HechoApiInputDto hechoDTO) {
        webApiCallerService.post(
                hechosServiceUrl + "/api/hechos",
                hechoDTO,
                Void.class
        );
    }


    public List<HechoApiOutputDto> getHechosByUsername(String username) {
        return webApiCallerService.getList(
                hechosServiceUrl + "/api/hechos/" + username,
                HechoApiOutputDto.class
        );
    }

    // Obtener todos los hechos
    public List<HechoApiOutputDto> obtenerHechos() {
        List<HechoApiOutputDto> response = webApiCallerService.getListWithoutToken(hechoTotalesServiceUrl + "/hechos", HechoApiOutputDto.class);
        if (response == null) {
            throw new RuntimeException("Error al obtener hechos del servicio externo");
        }
        return response;
    }

    public HechoApiOutputDto obtenerHechoPorId(Long id) {
        String url = "http://localhost:8080/hechos/" + id;
        HechoAGREInput response = webApiCallerService.getWithoutToken(url, HechoAGREInput.class);

        if (response == null) {
            throw new NotFoundException("Hechos");
        }

        HechoApiOutputDto dto = new HechoApiOutputDto();
        dto.setId(response.getId());
        dto.setTitulo(response.getTitulo());
        dto.setDescripcion(response.getDescripcion());
        dto.setUsername(response.getUsername());

        dto.setCategoria(response.getCategoria());


        // ubicación
        if (response.getUbicacion() != null) {
            dto.setUbicacion(response.getUbicacion());
        }

        dto.setFechaHecho(response.getFechaHecho());

        // multimedia
        if (response.getMultimedia() != null) {
            dto.setMultimedia(response.getMultimedia());
        }

        dto.setOrigen(response.getOrigen() != null ? response.getOrigen().name() : null);

        dto.setMostrarDatos(response.getMostrarDatos());
        dto.setEsValido(response.getEsValido());
        dto.setFechaCreacion(response.getFechaCreacion());
        return dto;
    }

    public HechoApiOutputDto obtenerHechoPorIdPorColeccion(Long id) {
        // Traemos la lista completa de hechos
        HechoApiOutputDto[] hechosArray = webApiCallerService.getWithoutToken(
                hechoTotalesServiceUrl + "colecciones/1/hechos",
                HechoApiOutputDto[].class
        );

        if (hechosArray == null || hechosArray.length == 0) {
            throw new NotFoundException("Hechos");
        }

        // Convertimos a lista y filtramos por id
        return Arrays.stream(hechosArray)
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Hecho con id " + id + " no encontrado"));
    }


    // Actualizar un hecho por ID
    public void actualizarHecho(Long id, HechoInputEditarApi hechoDTO) {
        webApiCallerService.put(
                hechosServiceUrl + "/api/hechos/" + id,
                hechoDTO,
                HechoOutputDTO.class
        );
    }

    public void importarHechosCSV(MultipartFile file) {

        // Apunta a http://localhost:8020/api/hechos/importar
        String apiUrl = hechosStaticServiceUrl + "/api/hechos/importar";
        log.info("Enviando archivo CSV al servicio ESTÁTICO: {}", apiUrl);

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            body.add("archivo", resource); // "archivo" coincide con el @RequestParam

            webClient.post()
                    .uri(apiUrl)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(body))
                    .retrieve()
                    .toBodilessEntity()
                    .block(); // Espera a que termine

        } catch (IOException e) {
            log.error("Error al leer los bytes del archivo", e);
            throw new RuntimeException("Error al leer el archivo: " + e.getMessage());
        } catch (WebClientResponseException e) {
            log.error("Error del backend ESTÁTICO (8020) al importar: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Falló la importación en el backend: " + e.getResponseBodyAsString());
        }

    }
}
