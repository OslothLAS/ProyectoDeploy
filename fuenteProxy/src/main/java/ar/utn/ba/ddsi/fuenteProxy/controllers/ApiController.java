package ar.utn.ba.ddsi.fuenteProxy.controllers;

import entities.hechos.Hecho;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ar.utn.ba.ddsi.fuenteProxy.services.IApiService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hechos")
public class ApiController {

    private final IApiService hechoService;

    public ApiController(IApiService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping
    public List<Hecho> obtenerHechos(@RequestParam Map<String, String> filtros) {
        return hechoService.getHechos(filtros);
    }

    @GetMapping("/origen")
    public String obtenerOrigen(){
        return "PROXY";
    }
}

