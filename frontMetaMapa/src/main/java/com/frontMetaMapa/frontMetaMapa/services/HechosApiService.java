package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
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

import java.util.List;


@Service
public class HechosApiService {
    private static final Logger log = LoggerFactory.getLogger(HechosApiService.class);

    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String hechosServiceUrl;

    @Autowired
    public HechosApiService(WebApiCallerService webApiCallerService,
                           @Value("${hechos.service.url}") String hechosServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.hechosServiceUrl = hechosServiceUrl;
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
    public List<HechoOutputDTO> obtenerHechos() {
        List<HechoOutputDTO> response = webApiCallerService.getList(hechosServiceUrl + "/hechos", HechoOutputDTO.class);
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

    // Actualizar un hecho por ID
    public HechoOutputDTO actualizarHecho(Long id, HechoInputDTO hechoDTO) {
        HechoOutputDTO response = webApiCallerService.put(
                hechosServiceUrl + "/hechos/" + id,
                hechoDTO,
                HechoOutputDTO.class
        );

        if (response == null) {
            throw new NotFoundException("Hecho", id.toString());
        }
        return response;
    }
}
