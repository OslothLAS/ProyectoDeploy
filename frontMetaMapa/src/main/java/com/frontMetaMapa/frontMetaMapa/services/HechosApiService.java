package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.input.HechoInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.HechoOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.internal.WebApiCallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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
    public HechoOutputDTO createHecho(HechoInputDTO hechoDTO) {
        HechoOutputDTO response = webApiCallerService.post(
                hechosServiceUrl + "/hechos",
                hechoDTO,
                HechoOutputDTO.class
        );

        if (response == null) {
            throw new RuntimeException("Error al crear hecho en el servicio externo");
        }
        return response;
    }

    // Obtener todos los hechos
    public List<HechoOutputDTO> obtenerHechos() {
        List<HechoOutputDTO> response = webApiCallerService.getList(hechosServiceUrl + "/hechos", HechoOutputDTO.class);
        if (response == null) {
            throw new RuntimeException("Error al obtener hechos del servicio externo");
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
