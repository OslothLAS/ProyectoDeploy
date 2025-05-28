package controllers;

import entities.hechos.Hecho;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.IExtractService;

import java.util.List;

@RestController
@RequestMapping("/hechos")
public class HechoController {

    private final IExtractService hechoService;

    public HechoController(IExtractService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping
    public List<Hecho> obtenerHechos() {
        return hechoService.getHechos();
    }
}
