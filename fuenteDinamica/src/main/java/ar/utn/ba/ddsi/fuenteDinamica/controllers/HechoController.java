package ar.utn.ba.ddsi.fuenteDinamica.controllers;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.TokenInfo;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;
import ar.utn.ba.ddsi.fuenteDinamica.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class HechoController {
    private final IHechoService hechoService;
    public HechoController(IHechoService hechoService) {
        this.hechoService = hechoService;
    }

    /*
    @GetMapping
    public List<HechoDTO> getHechos(@RequestParam Map<String, String> filtros){
        return hechosToDTO(this.hechoService.obtenerTodos(filtros));
    }*/

    @GetMapping
    public ResponseEntity<List<HechoDTO>> obtenerTodosLosHechos(){
        return ResponseEntity.ok(this.hechoService.getAllHechos());
    }

    @GetMapping("hecho/{id}")
    public ResponseEntity<HechoDTO> obtenerHechoPorId(@PathVariable("id") Long id) {
        HechoDTO hecho = this.hechoService.getHechoById(id);
        return ResponseEntity.ok(hecho);
    }


   /* @GetMapping("{id}")
    public ResponseEntity<HechoDTO> obtenerUsuarioPorId(@PathVariable("id") Long id) {
        HechoDTO hecho = this.hechoService.getHechoById(id);
        return ResponseEntity.ok(hecho);
    }*/

    @PostMapping
    public HechoOutputDTO crearHecho(@RequestBody HechoDTO hecho, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        TokenInfo tokenInfo = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            tokenInfo = JwtUtil.validarToken(token);
        }

        return this.hechoService.crearHecho(hecho, tokenInfo);
    }

    @PostMapping("/prueba")
    public ResponseEntity<TokenInfo> crearHecho2(@RequestHeader("Authorization") String authHeader) {
        System.out.println("Token: " + authHeader);

        String token = authHeader.replace("Bearer ", "");
        TokenInfo tokenInfo = JwtUtil.validarToken(token);

        return ResponseEntity.ok(tokenInfo);
    }

    @PutMapping("/{idHecho}")
    public ResponseEntity<Void> editarHecho(@PathVariable("idHecho") Long idHecho,
                                            @RequestBody HechoDTO hecho,
                                            @RequestHeader("Authorization") String authHeader) throws Exception {

        String token = authHeader.replace("Bearer ", "");
        TokenInfo tokenInfo = JwtUtil.validarToken(token);
        this.hechoService.editarHecho(idHecho, hecho,tokenInfo);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/origen")
    public String obtenerOrigen(){
        return "DINAMICO";
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<HechoDTO>> getHechosByUsername(@PathVariable("username") String username){
        List <HechoDTO> hechos = this.hechoService.getHechosByUsername(username);
        return ResponseEntity.ok(hechos);
    }

    @PutMapping("/invalidar")
    public void invalidarHechoPorTituloYDescripcion(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion) {
        hechoService.invalidarHechoPorTituloYDescripcion(titulo, descripcion);
    }
}
