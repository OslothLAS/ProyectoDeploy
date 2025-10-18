package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoInputEditarApi;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.HechoApiInputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.HechoInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoApiOutputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.internal.WebApiCallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;


@Service
public class HechosApiService {
    private static final Logger log = LoggerFactory.getLogger(HechosApiService.class);

    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String hechosServiceUrl;
    private final String hechoTotalesServiceUrl;


    @Autowired
    public HechosApiService(WebApiCallerService webApiCallerService,
                            @Value("${hechos.service.url}") String hechosServiceUrl,
                            @Value("${hechos.totales.service.url}") String hechoTotalesServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.hechosServiceUrl = hechosServiceUrl;
        this.hechoTotalesServiceUrl = hechoTotalesServiceUrl;
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
        HechoApiOutputDto response = webApiCallerService.getWithoutToken(
                hechosServiceUrl + "api/hechos/hecho/" + id,
                HechoApiOutputDto.class
        );
        if (response == null) {
            throw new NotFoundException("Hechos");
        }
        return response;
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
}
