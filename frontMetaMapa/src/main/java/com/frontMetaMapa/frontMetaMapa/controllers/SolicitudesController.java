package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoApiOutputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.SolicitudInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.HechoService;
import com.frontMetaMapa.frontMetaMapa.services.SolicitudEliminacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class SolicitudesController {

    private final SolicitudEliminacionService solicitudEliminacionService;
    private final HechoService hechoService;

    /**
     * Agrega el rol actual al modelo, para poder usarlo en la navbar o la vista.
     */
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

    /**
     * Muestra el formulario de solicitud de eliminación.
     */
    @GetMapping("/solicitar-eliminacion")
    public String solicitarEliminacion(@RequestParam Long id, Model model, Authentication authentication) {
        HechoApiOutputDto hecho = hechoService.obtenerHechoPorIdPorColeccion(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hecho no encontrado con id " + id));

        SolicitudInputDTO dto = new SolicitudInputDTO();
        dto.setIdHecho(id);

        // Si el usuario está autenticado, guardamos su username en el DTO
        if (authentication != null) {
            dto.setUsername(authentication.getName());
        }

        model.addAttribute("solicitudInputDTO", dto);
        model.addAttribute("tituloHecho", hecho.getTitulo());
        model.addAttribute("descripcionHecho", hecho.getDescripcion());

        return "contribuyente/solicitudEliminacion";
    }

    /**
     * Envía la solicitud de eliminación al servicio.
     */
    @PostMapping("/solicitar-eliminacion")
    public String crearSolicitud(@ModelAttribute SolicitudInputDTO solicitudInputDTO,
                                 Authentication authentication) {

        // Seteamos el username desde el contexto de seguridad (por seguridad)
        if (authentication != null) {
            solicitudInputDTO.setUsername(authentication.getName());
        }

        solicitudEliminacionService.crearSolicitud(solicitudInputDTO);
        return "redirect:/buscador-colecciones";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/solicitudEliminacion/{id}/aceptar")
    public String aceptarSolicitud(@PathVariable String id) {
        solicitudEliminacionService.aceptarSolicitud(id);
        return "redirect:/dashboard-solicitudes";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/solicitudEliminacion/{id}/rechazar")
    public String rechazarSolicitud(@PathVariable String id) {
        solicitudEliminacionService.rechazarSolicitud(id);
        return "redirect:/dashboard-solicitudes";
    }
}
