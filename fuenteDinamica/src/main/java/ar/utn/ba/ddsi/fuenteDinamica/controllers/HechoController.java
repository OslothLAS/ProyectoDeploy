package ar.utn.ba.ddsi.fuenteDinamica.controllers;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;
import entities.hechos.Hecho;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuentedinamica/hechos")
public class HechoController {
    private final IHechoService hechoService;
    public HechoController(IHechoService hechoService) {
        this.hechoService = hechoService;
    }
    @GetMapping
    public List<Hecho> getHechos(){
        return this.hechoService.obtenerTodos();
    }

    @PostMapping
    public void crearHecho(@RequestBody HechoInputDTO hecho) {
        this.hechoService.crearHecho(hecho);
    }

    @PutMapping("/{idHecho}")
    public void editarHecho(@PathVariable Long idHecho, @RequestBody HechoInputDTO hecho) throws Exception {
        this.hechoService.editarHecho(idHecho, hecho);
    }
}
