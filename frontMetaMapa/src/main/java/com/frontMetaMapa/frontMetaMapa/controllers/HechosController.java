package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoInputEditarApi;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.HechoInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoApiOutputDto;
import com.frontMetaMapa.frontMetaMapa.services.HechoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HechosController {
    private final HechoService hechoService;


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

    @GetMapping("/buscador-hechos")
    public String buscadorHechos() {
        return "commons/buscadorHechos";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/hecho/{id}/editar")
    public String editarHecho(@PathVariable Long id, Model model) {
        model.addAttribute("idHecho", id);
        return "commons/editarHecho";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @PostMapping("/hecho")
    public String crearHecho(@ModelAttribute HechoInputDTO hechoInputDTO) {
        hechoService.crearHecho(hechoInputDTO);
        return "commons/buscadorHechos"; // o redirección al mensaje de éxito
    }

    @GetMapping("/mis-contribuciones")
    public String misContribuciones(Model model, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            // Redirigir al login si no hay usuario
            return "redirect:/login";
        }
        List<HechoApiOutputDto> hechos = hechoService.obtenerHechosPorUsername(username);
        model.addAttribute("hechos", hechos);
        model.addAttribute("username", username);
        return "contribuyente/misContribuciones"; // Thymeleaf template
    }

    @GetMapping("/hecho/{id}")
    public String detalleHecho(@PathVariable Long id, Model model, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        try {
            // Obtener el hecho directamente por ID
            Optional<HechoApiOutputDto> hechoOpt = hechoService.obtenerHechoPorId(id);

            if (hechoOpt.isPresent()) {
                model.addAttribute("hecho", hechoOpt.get());
                model.addAttribute("username", username);
                return "commons/detalleHecho";
            } else {
                // Si no encuentra el hecho, redirigir con error
                return "redirect:/mis-contribuciones?error=hecho-no-encontrado";
            }
        } catch (Exception e) {
            return "redirect:/mis-contribuciones?error=error-servidor";
        }
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("/edicionHecho/{id}")
    public String editarHecho(@PathVariable Long id, Model model, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        try {
            // Obtener el hecho por ID
            Optional<HechoApiOutputDto> hechoOpt = hechoService.obtenerHechoPorId(id);

            if (hechoOpt.isPresent()) {
                HechoApiOutputDto hecho = hechoOpt.get();

                // Opcional: verificar si el usuario tiene permisos para editar este hecho
                if (!hecho.getUsername().equals(username)) {
                    return "redirect:/mis-contribuciones?error=sin-permiso";
                }

                model.addAttribute("hecho", hecho);
                model.addAttribute("username", username);
                return "contribuyente/editarHecho";
            } else {
                return "redirect:/mis-contribuciones?error=hecho-no-encontrado";
            }
        } catch (Exception e) {
            return "redirect:/mis-contribuciones?error=error-servidor";
        }
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @PostMapping("/hechoEdicion/{id}")
    public String editarHecho(
            @PathVariable Long id,
            @ModelAttribute HechoInputEditarApi hechoInputDTO,
            HttpServletRequest request,
            Model model) {

        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
            hechoService.actualizarHecho(id, hechoInputDTO);
            // Redirigir a la página de contribuciones con mensaje de éxito
            return "redirect:/mis-contribuciones?success=hecho-actualizado";

    }
}
