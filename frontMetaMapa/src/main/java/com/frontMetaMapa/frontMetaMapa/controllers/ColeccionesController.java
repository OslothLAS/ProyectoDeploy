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
            Model model) {

        ColeccionOutputDTO coleccion = coleccionService.obtenerColeccionPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con id " + id));

        List<HechoOutputDTO> hechos = coleccionService.obtenerHechosPorColeccionId(id, fuente, fechaFin, categoria);

        model.addAttribute("idColeccion", id);
        model.addAttribute("tituloColeccion", coleccion.getTitulo());
        model.addAttribute("descripcionColeccion", coleccion.getDescripcion());
        model.addAttribute("hechos", hechos);

        // ⚡ Pasamos los filtros al template
        model.addAttribute("filtroFuente", fuente);
        model.addAttribute("filtroFechaFin", fechaFin);
        model.addAttribute("filtroCategoria", categoria);

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
        System.out.println(coleccionInputDTO);
        coleccionService.crearColeccion(coleccionInputDTO);
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


    // Manejar envío del formulario
    @PostMapping("/editar/{id}")
    public String editarColeccion(
            @PathVariable("id") Long id,
            @ModelAttribute("coleccion") ColeccionInputDTO dto,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ColeccionOutputDTO actualizado = coleccionService.actualizarColeccion(id, dto);
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








