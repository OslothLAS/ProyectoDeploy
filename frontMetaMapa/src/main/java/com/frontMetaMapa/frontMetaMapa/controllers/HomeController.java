package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.exceptions.RateLimitException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoApiOutputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.AuthResponseDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.UsuarioDTO;
import com.frontMetaMapa.frontMetaMapa.services.ColeccionService;
import com.frontMetaMapa.frontMetaMapa.services.HechoService;
import com.frontMetaMapa.frontMetaMapa.services.LoginApiService;
import com.frontMetaMapa.frontMetaMapa.services.RegisterApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RegisterApiService registerApiService;
    private final HechoService hechoService;
    private final LoginApiService loginApiService;
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

    @GetMapping("/")
    public String home(Model model) {
        List<HechoApiOutputDto> hechos = hechoService.obtenerTodosLosHechos();

        // Tomar solo los primeros 3
        List<HechoApiOutputDto> primerosTres = hechos.stream()
                .limit(3)
                .toList(); // .collect(Collectors.toList()) si usás Java < 16

        model.addAttribute("hechos", primerosTres);
        return "visualizador/index";
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
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpServletRequest request,
                              Model model) {
        try {
            AuthResponseDTO authResponse = loginApiService.login(username, password);

            // 2. Guardar tokens en la sesión del Frontend
            HttpSession session = request.getSession();
            session.setAttribute("accessToken", authResponse.getAccessToken());
            session.setAttribute("refreshToken", authResponse.getRefreshToken());
            session.setAttribute("username", username);

            return "redirect:/";

        } catch (RateLimitException e) {

            model.addAttribute("error", "Demasiados intentos fallidos. Por favor espera " + e.getSegundos() + " segundos.");
            return "login"; // Volvemos a mostrar el formulario

        } catch (Exception e) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    // 👉 Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login?logout";
    }
}



