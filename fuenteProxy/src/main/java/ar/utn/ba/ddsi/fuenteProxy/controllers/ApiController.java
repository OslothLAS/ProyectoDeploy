package ar.utn.ba.ddsi.fuenteProxy.controllers;

import ar.utn.ba.ddsi.fuenteProxy.dtos.hecho.HechoOutputDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ar.utn.ba.ddsi.fuenteProxy.services.IApiService;
import java.util.List;
import java.util.Map;
import static ar.utn.ba.ddsi.fuenteProxy.mappers.HechoUtil.hechosToDTO;

@RestController
@RequestMapping("/api/hechos")
public class ApiController {

    private final IApiService hechoService;

    public ApiController(IApiService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping
    public List<HechoOutputDTO> obtenerHechos(@RequestParam Map<String, String> filtros) {
        return hechosToDTO(hechoService.getHechos(filtros));
    }

    @GetMapping("/origen")
    public String obtenerOrigen(){
        return "PROXY";
    }
}

