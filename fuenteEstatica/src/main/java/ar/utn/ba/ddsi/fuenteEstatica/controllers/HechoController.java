package ar.utn.ba.ddsi.fuenteEstatica.controllers;

import ar.utn.ba.ddsi.fuenteEstatica.entities.dtos.HechoOutputDTO;
import ar.utn.ba.ddsi.fuenteEstatica.entities.hechos.Hecho;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ar.utn.ba.ddsi.fuenteEstatica.services.IExtractService;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        // Obtener hechos desde las dos fuentes
        List<Hecho> hechosBD = hechoService.getHechos(filtros);
        List<Hecho> hechosMemoria = hechoService.getHechosMemoria(filtros);

        List<Hecho> hechosCombinados = new ArrayList<>();
        hechosCombinados.addAll(hechosBD);
        hechosCombinados.addAll(hechosMemoria);

        // Convertir a DTO y devolver
        return hechosToDTO(hechosCombinados);
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

    @PostMapping("/importar")
    public ResponseEntity<?> importarHechos(@RequestParam("archivo") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Archivo vacío"));
        }
        try {

            hechoService.importarHechosDesdeArchivo(
                    file.getInputStream(),
                    file.getOriginalFilename()
            );
            return ResponseEntity.ok(Map.of("message", "Hechos importados correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error al procesar el archivo: " + e.getMessage()));
        }
    }
}