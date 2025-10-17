package ar.utn.ba.ddsi.fuenteDinamica.controllers;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.TokenInfo;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;
import ar.utn.ba.ddsi.fuenteDinamica.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import static ar.utn.ba.ddsi.fuenteDinamica.utils.HechoUtil.hechosToOutputDTO;

@RestController
@RequestMapping("/api/hechos")
public class HechoController {
    private final IHechoService hechoService;
    public HechoController(IHechoService hechoService) {
        this.hechoService = hechoService;
    }


    @GetMapping
    public List<HechoOutputDTO> getHechos(@RequestParam Map<String, String> filtros){
        return hechosToOutputDTO(this.hechoService.obtenerTodos(filtros));
    }


    @GetMapping("hecho/{id}")
    public ResponseEntity<HechoOutputDTO> obtenerHechoPorId(@PathVariable("id") Long id) {
        HechoOutputDTO hecho = this.hechoService.getHechoById(id);
        return ResponseEntity.ok(hecho);
    }


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

    @GetMapping("/{username}")
    public ResponseEntity<List<HechoOutputDTO>> getHechosByUsername(@PathVariable("username") String username){
        List <HechoOutputDTO> hechos = this.hechoService.getHechosByUsername(username);
        return ResponseEntity.ok(hechos);
    }

    @PutMapping("/invalidar")
    public void invalidarHechoPorTituloYDescripcion(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion) {
        hechoService.invalidarHechoPorTituloYDescripcion(titulo, descripcion);
    }

    @GetMapping("/origen")
    public String obtenerOrigen(){
        return "DINAMICO";
    }
}
