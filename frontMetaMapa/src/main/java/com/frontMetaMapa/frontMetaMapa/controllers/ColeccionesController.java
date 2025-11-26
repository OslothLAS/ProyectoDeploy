package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.input.ColeccionInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.CriterioDePertenenciaInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.FuenteInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.HechoInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.CriterioDePertenenciaOutputDTO;

import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ColeccionesController {
    private final ColeccionService coleccionService;
    @Value("${fuenteEstatica.service.url}")
    private String fuenteEstaticaUrl;

    @Value("${fuenteDinamica.service.url}")
    private String fuenteDinamicaUrl;

    @Value("${fuenteProxy.service.url}")
    private String fuenteProxyUrl;

    @ModelAttribute
    public void addRolToModel(Model model, Authentication authentication) {
        if (authentication != null) {
            String rol = authentication.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("SIN_ROL");
            model.addAttribute("rol", rol);
        } else {
            model.addAttribute("rol", "ANONIMO");
        }
    }

    @GetMapping("/buscador-colecciones")
    public String buscadorColecciones(Model model) {
        List<ColeccionOutputDTO> colecciones = coleccionService.obtenerTodasLasColecciones();
        System.out.println("🔍 Colecciones encontradas: " + colecciones);
        model.addAttribute("colecciones", colecciones);
        return "commons/buscadorColecciones";
    }
    @GetMapping("/colecciones/show")
    public String showColeccion(
            @RequestParam Long id,
            @RequestParam(required = false) String fuente,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String modoNavegacion,
            Model model) {

        ColeccionOutputDTO coleccion = coleccionService.obtenerColeccionPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con id " + id));

        String fuenteLimpia = ("null".equals(fuente) || fuente == null) ? null : fuente;
        String fechaFinLimpia = ("null".equals(fechaFin) || fechaFin == null) ? null : fechaFin;
        String categoriaLimpia = ("null".equals(categoria) || categoria == null) ? null : categoria;
        String modoLimpio = ("null".equals(modoNavegacion) || modoNavegacion == null) ? null : modoNavegacion;

        List<HechoOutputDTO> hechos = coleccionService.obtenerHechosPorColeccionId(
                id, fuenteLimpia, fechaFinLimpia, categoriaLimpia, modoLimpio);

        model.addAttribute("idColeccion", id);
        model.addAttribute("tituloColeccion", coleccion.getTitulo());
        model.addAttribute("descripcionColeccion", coleccion.getDescripcion());
        model.addAttribute("hechos", hechos);

        // ⚡ Pasamos los filtros al template
        model.addAttribute("filtroFuente", fuenteLimpia != null ? fuenteLimpia : "");
        model.addAttribute("filtroFechaFin", fechaFinLimpia != null ? fechaFinLimpia : "");
        model.addAttribute("filtroCategoria", categoriaLimpia != null ? categoriaLimpia : "");

        // Determinar el modo actual
        String modo = "CURADO".equals(modoLimpio) ? "curado" : "irrestricto";
        model.addAttribute("modo", modo);

        return "commons/showColeccion";
    }



    @GetMapping("/crearColeccion")
    public String nuevaColeccion(Model model) {
        ColeccionInputDTO coleccionDTO = new ColeccionInputDTO();
        // Inicializamos listas vacías para Thymeleaf
        coleccionDTO.setFuentes(new ArrayList<>());
        coleccionDTO.setCriterios(new ArrayList<>());
        // Agregamos un criterio de categoría por defecto
        CriterioDePertenenciaInputDTO criterio = new CriterioDePertenenciaInputDTO();
        criterio.setTipo("categoria");
        criterio.setCategoria("");
        coleccionDTO.getCriterios().add(criterio);

        model.addAttribute("coleccion", coleccionDTO);
        return "administrador/createColeccion"; // nombre del HTML Thymeleaf
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/createColeccion")
    public String crearColeccion(@ModelAttribute ColeccionInputDTO coleccionInputDTO) {

        // Si hay fuentes, las procesamos
        if (coleccionInputDTO.getFuentes() != null) {
            for (FuenteInputDTO fuente : coleccionInputDTO.getFuentes()) {
                if (fuente.getOrigen() != null) {
                    switch (fuente.getOrigen()) {
                        case ESTATICO -> fuente.setUrl(fuenteEstaticaUrl);
                        case DINAMICO -> fuente.setUrl(fuenteDinamicaUrl);
                        case PROXY    -> fuente.setUrl(fuenteProxyUrl);
                        default       -> fuente.setUrl(null);
                    }
                }
            }
        }

        coleccionService.crearColeccion(coleccionInputDTO);
        return "redirect:/buscador-colecciones";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/deleteColeccion/{id}")
    public String deleteColeccion(@PathVariable("id") Long id) {
        coleccionService.eliminarColeccion(id);
        return "redirect:/buscador-colecciones"; // o redirección al mensaje de éxito
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/colecciones/{id}/editar")
    public String editarColeccion(@PathVariable Long id, Model model) {
        ColeccionOutputDTO coleccion = coleccionService.obtenerColeccionPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada"));

        model.addAttribute("coleccion", coleccion); // <-- clave "coleccion" para Thymeleaf
        return "administrador/editarColeccion";
    }


    @PostMapping("/editar/{id}")
    public String editarColeccion(
            @PathVariable("id") Long id,
            @ModelAttribute("coleccion") ColeccionInputDTO dto,
            RedirectAttributes redirectAttributes
    ) {
        try {

            // ✔ Convertimos origen → url real
            if (dto.getFuentes() != null) {
                dto.getFuentes().forEach(f -> {
                    if (f.getOrigen() != null) {
                        switch (f.getOrigen()) {
                            case ESTATICO -> f.setUrl(fuenteEstaticaUrl);
                            case DINAMICO -> f.setUrl(fuenteDinamicaUrl);
                            case PROXY -> f.setUrl(fuenteProxyUrl);
                            default -> f.setUrl(null);
                        }
                    }
                });
            }

            // ✔ Guardamos
            coleccionService.actualizarColeccion(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Colección actualizada correctamente");

            return "redirect:/colecciones/show?id=" + id;

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la colección");
            return "redirect:/colecciones/editar/" + id;
        }
    }
}



/**
 * Guardar colección (crear o actualizar)
 */
    /*
    @PostMapping
    public String guardarColeccion(@ModelAttribute ColeccionInputDTO coleccionDTO) {
        Long id = coleccionDTO.getId();
        coleccionService.actualizarColeccion(id, coleccionDTO);
        return "redirect:/colecciones"; // lista de colecciones
    }*/








