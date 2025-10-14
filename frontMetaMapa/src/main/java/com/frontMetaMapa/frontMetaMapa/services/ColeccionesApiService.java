package com.frontMetaMapa.frontMetaMapa.services;


import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.input.ColeccionInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.AuthResponseDTO;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.RolesPermisosDTO;
import com.frontMetaMapa.frontMetaMapa.services.internal.WebApiCallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class ColeccionesApiService {
    private static final Logger log = LoggerFactory.getLogger(ColeccionesApiService.class);
    private final WebApiCallerService webApiCallerService;

    private final String coleccionesServiceUrl;
    @Autowired
    public ColeccionesApiService(WebApiCallerService webApiCallerService,
                                 @Value("${colecciones.service.url}") String coleccionesServiceUrl) {
        this.webApiCallerService = webApiCallerService;

        this.coleccionesServiceUrl = coleccionesServiceUrl;
    }


    public List<ColeccionOutputDTO> obtenerTodasLasColecciones() {
        List<ColeccionOutputDTO> response = webApiCallerService.getList(coleccionesServiceUrl + "/colecciones", ColeccionOutputDTO.class);
        return response != null ? response : List.of();
    }

    public ColeccionOutputDTO obtenerColeccionPorId(String id) {
        ColeccionOutputDTO response = webApiCallerService.get(coleccionesServiceUrl + "/colecciones/" + id, ColeccionOutputDTO.class);
        if (response == null) {
            throw new NotFoundException("Coleccion", id);
        }
        return response;
    }

    public ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionDTO) {
        ColeccionOutputDTO response = webApiCallerService.post(coleccionesServiceUrl + "/colecciones", coleccionDTO, ColeccionOutputDTO.class);
        if (response == null) {
            throw new RuntimeException("Error al crear coleccion en el servicio externo");
        }
        return response;
    }

    public ColeccionOutputDTO actualizarColeccion(String id, ColeccionInputDTO coleccionDTO) {
        ColeccionOutputDTO response = webApiCallerService.put(coleccionesServiceUrl + "/colecciones/" + id, coleccionDTO, ColeccionOutputDTO.class);
        if (response == null) {
            throw new RuntimeException("Error al actualizar coleccion en el servicio externo");
        }
        return response;
    }

    public void eliminarColeccion(String id) {
        webApiCallerService.delete(coleccionesServiceUrl + "/colecciones/" + id);
    }

    public boolean existeColeccion(String id) {
        try {
            obtenerColeccionPorId(id);
            return true;
        } catch (NotFoundException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia del coleccion: " + e.getMessage(), e);
        }
    }
}
