package ar.utn.ba.ddsi.fuenteProxy.controllers;

import ar.utn.ba.ddsi.fuenteProxy.services.IMetamapaService;
import entities.hechos.Hecho;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.utn.ba.ddsi.fuenteProxy.services.IApiService;

import java.util.List;

@RestController
@RequestMapping("/metamapa/hechos")
public class MetamapaController {

    private final IMetamapaService MetemapaService;

    public MetamapaController(IMetamapaService MetemapaService) {
        this.MetemapaService = MetemapaService;
    }

    @GetMapping
    public List<Hecho> obtenerHechos() {
        return MetemapaService.getHechos();
    }
}




