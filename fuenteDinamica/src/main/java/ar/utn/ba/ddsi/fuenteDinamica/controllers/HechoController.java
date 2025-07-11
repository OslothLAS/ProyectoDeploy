package ar.utn.ba.ddsi.fuenteDinamica.controllers;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoInputDTO;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;
import entities.hechos.Hecho;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hechos")
public class HechoController {
    private final IHechoService hechoService;
    public HechoController(IHechoService hechoService) {
        this.hechoService = hechoService;
    }
    @GetMapping
    public List<Hecho> getHechos(@RequestParam Map<String, String> filtros){
        return this.hechoService.obtenerTodos(filtros);
    }

    @PostMapping
    public void crearHecho(@RequestBody HechoInputDTO hecho) {
        this.hechoService.crearHecho(hecho);
    }

    @PutMapping("/{idHecho}")
    public void editarHecho(@PathVariable Long idHecho, @RequestBody HechoInputDTO hecho) throws Exception {
        this.hechoService.editarHecho(idHecho, hecho);
    }

    @GetMapping("/origen")
    public String obtenerOrigen(){
        return "DINAMICO";
    }

    @PutMapping("/invalidar")
    public void invalidarHechoPorTituloYDescripcion(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion) {
        hechoService.invalidarHechoPorTituloYDescripcion(titulo, descripcion);
    }
}
