package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.output.AuthResponseDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.UsuarioDTO;
import com.frontMetaMapa.frontMetaMapa.services.ColeccionService;
import com.frontMetaMapa.frontMetaMapa.services.LoginApiService;
import com.frontMetaMapa.frontMetaMapa.services.RegisterApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ColeccionService coleccionService;
    private final RegisterApiService registerApiService;
    private final LoginApiService loginApiService;

    @GetMapping("/")
    public String home() {
        return "redirect:/visualizador";
    }

    @GetMapping("/404")
    public String notFound(Model model) {
        model.addAttribute("titulo", "No encontrado");
        return "404";
    }

    @GetMapping("/403")
    public String accessDenied(Model model) {
        model.addAttribute("titulo", "Acceso denegado");
        model.addAttribute("mensaje", "No tiene permiso para acceder a este recurso.");
        return "403";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute UsuarioDTO request, Model model) {
        System.out.println("🎯 === POST /register INICIADO ===");
        System.out.println("📝 Datos recibidos:");
        System.out.println("  Username: " + request.getUsername());
        System.out.println("  Nombre: " + request.getNombre());
        System.out.println("  Apellido: " + request.getApellido());
        System.out.println("  Fecha: " + request.getFechaNacimiento());
        System.out.println("  Rol: " + request.getRol());

        try {
            boolean success = registerApiService.registerUser(request);
            System.out.println("✅ Resultado del servicio: " + success);

            if (success) {
                model.addAttribute("mensaje", "¡Registro exitoso!");
                return "login";
            } else {
                model.addAttribute("error", "El usuario ya existe");
                return "register";
            }
        } catch (Exception e) {
            System.out.println("💥 ERROR: " + e.getMessage());
            e.printStackTrace(); // ESTO ES IMPORTANTE
            model.addAttribute("error", "Error: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    // 👉 Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login?logout";
    }
}

/*
    @GetMapping("/buscador-colecciones")
    public String buscadorColecciones(Model model) {
        List<ColeccionOutputDTO> colecciones = coleccionService.obtenerTodasLasColecciones();
        model.addAttribute("colecciones", colecciones);
        return "commons/buscadorColecciones";
    }


    @GetMapping("/buscador-hechos")
    public String buscadorHechos() {
        return "commons/buscadorHechos";
    }

    /*
    @GetMapping("/hecho/{id}")
    public String detalleHecho(@PathVariable Long id, Model model) {
        model.addAttribute("idHecho", id);
        return "commons/detalleHecho";
    }

    @GetMapping("/coleccion/{id}/editar")
    public String editarColeccion(@PathVariable Long id, Model model) {
        model.addAttribute("idColeccion", id);
        return "commons/editarColeccion";
    }

    @GetMapping("/hecho/{id}/editar")
    public String editarHecho(@PathVariable Long id, Model model) {
        model.addAttribute("idHecho", id);
        return "commons/editarHecho";
    }

    @GetMapping("/mis-contribuciones")
    public String misContribuciones() {
        return "commons/misContribuciones";
    }


    @GetMapping("/coleccion/example")
    public String showColeccionExample() {
        return "commons/showColeccionExample";
    }
*/

