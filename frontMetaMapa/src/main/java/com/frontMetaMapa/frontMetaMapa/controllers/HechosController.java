package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoInputEditarApi;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.HechoInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoApiOutputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoMapaOutputDto;
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
import java.util.stream.Collectors;

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
    public String buscadorHechos(Model model) {
        List<HechoApiOutputDto> hechos = hechoService.obtenerTodosLosHechos();

        // 🔍 AÑADE ESTOS DEBUGGINGS:
        System.out.println("=== DEBUG CONTROLLER ===");
        System.out.println("Hechos del servicio: " + hechos.size());

        if (!hechos.isEmpty()) {
            System.out.println("Primer hecho: " + hechos.get(0));
            System.out.println("Título: " + hechos.get(0).getTitulo());
            System.out.println("Ubicación: " + hechos.get(0).getUbicacion());
        }

        List<HechoMapaOutputDto> hechosMapa = mapHechosToMapa(hechos);

        System.out.println("Hechos después del mapeo: " + hechosMapa.size());
        if (!hechosMapa.isEmpty()) {
            System.out.println("Primer hecho mapeado: " + hechosMapa.get(0));
        }

        model.addAttribute("hechos", hechosMapa);
        System.out.println("=== FIN DEBUG ===");

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
    public String crearHecho(@ModelAttribute HechoInputDTO hechoInputDTO, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        hechoInputDTO.setUsername(username);
        hechoService.crearHecho(hechoInputDTO, username);
        return "redirect:/mis-contribuciones";
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
            Optional<HechoApiOutputDto> hechoOpt = hechoService.obtenerHechoDinamicoPorId(id);

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

    @GetMapping("/hechoColeccion/{id}")
    public String detalleHechoColeccion(@PathVariable Long id, Model model, HttpServletRequest request) {
            Optional<HechoApiOutputDto> hechoOpt = hechoService.obtenerHechoPorId(id);
                model.addAttribute("hecho", hechoOpt.get());
                return "commons/detalleHecho";
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
            Optional<HechoApiOutputDto> hechoOpt = hechoService.obtenerHechoDinamicoPorId(id);

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

    private List<HechoMapaOutputDto> mapHechosToMapa(List<HechoApiOutputDto> hechos) {
        if (hechos == null) return List.of();

        // DEBUG
        System.out.println("=== INICIO MAPEO ===");
        System.out.println("Hechos a mapear: " + hechos.size());

        List<HechoMapaOutputDto> resultado = hechos.stream()
                .filter(h -> h.getUbicacion() != null)
                .filter(h -> isValidDouble(h.getUbicacion().getLatitud()) && isValidDouble(h.getUbicacion().getLongitud()))
                .map(h -> {
                    // DEBUG de cada hecho
                    System.out.println("Procesando: " + h.getTitulo());
                    System.out.println("Latitud original: '" + h.getUbicacion().getLatitud() + "'");
                    System.out.println("Longitud original: '" + h.getUbicacion().getLongitud() + "'");

                    HechoMapaOutputDto dto = new HechoMapaOutputDto();
                    dto.setId(h.getId());
                    dto.setTitulo(h.getTitulo());
                    dto.setDescripcion(h.getDescripcion());
                    dto.setCategoria(h.getCategoria());
                    dto.setLatitud(parseDoubleSafe(h.getUbicacion().getLatitud()));
                    dto.setLongitud(parseDoubleSafe(h.getUbicacion().getLongitud()));

                    System.out.println("Latitud convertida: " + dto.getLatitud());
                    System.out.println("Longitud convertida: " + dto.getLongitud());
                    System.out.println("---");

                    return dto;
                })
                .collect(Collectors.toList());

        System.out.println("Hechos mapeados exitosamente: " + resultado.size());
        System.out.println("=== FIN MAPEO ===");

        return resultado;
    }

    private Double parseDoubleSafe(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            // Reemplazar coma por punto y eliminar espacios
            String normalized = value.trim().replace(',', '.');
            return Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            System.out.println("❌ Error parseando '" + value + "': " + e.getMessage());
            return null;
        }
    }

    private boolean isValidDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            String normalized = value.trim().replace(',', '.');
            Double.parseDouble(normalized);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("❌ Valor inválido '" + value + "': " + e.getMessage());
            return false;
        }
    }
}
