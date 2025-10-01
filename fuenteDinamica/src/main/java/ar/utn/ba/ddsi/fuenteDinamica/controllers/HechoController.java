package ar.utn.ba.ddsi.fuenteDinamica.controllers;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import static ar.utn.ba.ddsi.fuenteDinamica.utils.HechoUtil.hechosToDTO;


@RestController
@RequestMapping("/api/hechos")
public class HechoController {
    private final IHechoService hechoService;
    public HechoController(IHechoService hechoService) {
        this.hechoService = hechoService;
    }
    @GetMapping
    public List<HechoDTO> getHechos(@RequestParam Map<String, String> filtros){
        return hechosToDTO(this.hechoService.obtenerTodos(filtros));
    }

    @PostMapping
    public void crearHecho(@RequestBody HechoDTO hecho) {
        this.hechoService.crearHecho(hecho);
    }

    @PutMapping("/{idHecho}")
    public void editarHecho(@PathVariable Long idHecho, @RequestBody HechoDTO hecho) throws Exception {
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
