package ar.utn.ba.ddsi.fuenteEstatica.controllers;

import entities.dtos.HechoOutputDTO;
import org.springframework.web.bind.annotation.*;
import ar.utn.ba.ddsi.fuenteEstatica.services.IExtractService;
import java.util.List;
import java.util.Map;
import static utils.HechoUtil.hechosToDTO;

@RestController
@RequestMapping("/api/hechos")
public class HechoController {

    private final IExtractService hechoService;

    public HechoController(IExtractService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping
    public List<HechoOutputDTO> obtenerHechos(@RequestParam Map<String, String> filtros) {
        return hechosToDTO(hechoService.getHechos(filtros));
    }

    @GetMapping("/origen")
    public String obtenerOrigen(){
        return "ESTATICO";
    }

    @PutMapping("/invalidar")
    public void invalidarHechoPorTituloYDescripcion(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion) {
        hechoService.invalidarHechoPorTituloYDescripcion(titulo, descripcion);
    }
}