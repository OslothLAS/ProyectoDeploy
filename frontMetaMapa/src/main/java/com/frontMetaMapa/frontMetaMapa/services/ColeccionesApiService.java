package com.frontMetaMapa.frontMetaMapa.services;


import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.ColeccionInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.AuthResponseDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.UserRolesPermissionsDTO;
import com.frontMetaMapa.frontMetaMapa.services.internal.WebApiCallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
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
        List<ColeccionOutputDTO> response = webApiCallerService.getListWithoutToken(
                coleccionesServiceUrl + "/colecciones",
                ColeccionOutputDTO.class
        );
        return response != null ? response : List.of();
    }

    // ColeccionApiService o APIService
    public List<HechoOutputDTO> obtenerHechosPorColeccionId(Long idColeccion,
                                                            String fuente,
                                                            String fechaInicio,
                                                            String fechaFin,
                                                            String categoria,
                                                            String modoNavegacion) {
        StringBuilder url = new StringBuilder(coleccionesServiceUrl)
                .append("/colecciones/")
                .append(idColeccion)
                .append("/hechos");

        List<String> params = new ArrayList<>();
        if (fuente != null && !fuente.isBlank() && !"null".equals(fuente)) {
            params.add("fuente=" + fuente);
        }
        if (fechaInicio != null && !fechaInicio.isBlank() && !"null".equals(fechaInicio)) {
            params.add("fechaInicio=" + fechaInicio);
        }
        if (fechaFin != null && !fechaFin.isBlank() && !"null".equals(fechaFin)) {
            params.add("fechaFin=" + fechaFin);
        }
        if (categoria != null && !categoria.isBlank() && !"null".equals(categoria)) {
            params.add("categoria=" + categoria);
        }
        if (modoNavegacion != null && !modoNavegacion.isBlank() && !"null".equals(modoNavegacion)) {
            params.add("modoNavegacion=" + modoNavegacion);
        }

        if (!params.isEmpty()) {
            url.append("?").append(String.join("&", params));
        }

        List<HechoOutputDTO> response = webApiCallerService.getListWithoutToken(
                url.toString(),
                HechoOutputDTO.class
        );

        return response != null ? response : List.of();
    }


    public ColeccionOutputDTO obtenerColeccionPorId(Long id) {
        ColeccionOutputDTO response = webApiCallerService.getWithoutToken(
                coleccionesServiceUrl + "/colecciones/" + id,
                ColeccionOutputDTO.class
        );
        if (response == null) {
            throw new NotFoundException("Coleccion");
        }
        return response;
    }


    public void crearColeccion(ColeccionInputDTO coleccionDTO) {
        System.out.println("llego esto: " + coleccionDTO);
        webApiCallerService.post(coleccionesServiceUrl + "/colecciones", coleccionDTO, Void.class);
    }

    public ColeccionOutputDTO actualizarColeccion(Long id, ColeccionInputDTO coleccionDTO) {
        ColeccionOutputDTO response = webApiCallerService.put(coleccionesServiceUrl + "/colecciones/" + id, coleccionDTO, ColeccionOutputDTO.class);
        if (response == null) {
            throw new RuntimeException("Error al actualizar coleccion en el servicio externo");
        }
        return response;
    }

    public void eliminarColeccion(Long id) {
        webApiCallerService.delete(coleccionesServiceUrl + "/colecciones/" + id);
    }

    public boolean existeColeccion(Long id) {
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
