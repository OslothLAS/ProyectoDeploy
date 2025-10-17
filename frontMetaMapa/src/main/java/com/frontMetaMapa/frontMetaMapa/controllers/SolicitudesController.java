package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.input.HechoInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.SolicitudInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.HechoService;
import com.frontMetaMapa.frontMetaMapa.services.SolicitudEliminacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SolicitudesController {
    private final SolicitudEliminacionService solicitudEliminacionService;
    private final HechoService hechoService;

    @GetMapping("/solicitar-eliminacion")
    public String solicitarEliminacion(@RequestParam Long id, Model model, HttpServletRequest request) {
        HechoOutputDTO hecho = hechoService.obtenerHechoPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hecho no encontrado con id " + id));

        // Crear DTO y setear el idHecho
        SolicitudInputDTO dto = new SolicitudInputDTO();
        dto.setIdHecho(id);

        // Obtener username (que en tu caso es el id del usuario) desde la sesión
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no logueado");
        }
        dto.setIdSolicitante(Long.valueOf(username)); // 👈 convertir a Long si tu username es numérico

        // Pasar datos a la vista
        model.addAttribute("solicitudInputDTO", dto);
        model.addAttribute("tituloHecho", hecho.getTitulo());
        model.addAttribute("descripcionHecho", hecho.getDescripcion());

        return "contribuyente/solicitudEliminacion";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @PostMapping("/solicitar-eliminacion")
    public String crearSolicitud(@ModelAttribute SolicitudInputDTO solicitudInputDTO) {
        solicitudEliminacionService.crearSolicitud(solicitudInputDTO);
        return "redirect:/visualizador"; // redirigir a donde quieras
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/solicitudEliminacion/{id}/aceptar")
    public String aceptarSolicitud(@PathVariable String id, Model model, HttpServletRequest request) {
        solicitudEliminacionService.aceptarSolicitud(id);
        return "redirect:/administrador/dashboard-solicitudes";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/solicitudEliminacion/{id}/rechazar")
    public String rechazarSolicitud(@PathVariable String id, Model model, HttpServletRequest request) {
        solicitudEliminacionService.rechazarSolicitud(id);
        return "redirect:/dashboard-solicitudes";
    }


}
