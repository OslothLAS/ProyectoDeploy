package ar.utn.ba.ddsi.fuenteEstatica.controllers;

import entities.hechos.Hecho;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ar.utn.ba.ddsi.fuenteEstatica.services.IExtractService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hechos")
public class HechoController {

    private final IExtractService hechoService;

    public HechoController(IExtractService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping
    public List<Hecho> obtenerHechos(@RequestParam Map<String, String> filtros) {
        return hechoService.getHechos(filtros);
    }
}
