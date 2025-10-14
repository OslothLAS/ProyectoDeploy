package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.UsuarioDTO;
import com.frontMetaMapa.frontMetaMapa.services.ColeccionService;
import com.frontMetaMapa.frontMetaMapa.services.RegisterApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ColeccionService coleccionService;
    private final RegisterApiService registerApiService;

    @GetMapping("/")
    public String home() {
        return "redirect:/visualizador";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute UsuarioDTO request) {
        System.out.println("POST /register recibido!");
        System.out.println("UsuarioDTO: " + request);
        boolean success = registerApiService.registerUser(request);
        if (success) {
            return "login"; // redirigís al login u otra vista
        } else {
            return "register"; // mostrás el error en el mismo formulario
        }
    }

    @GetMapping("/login")
    public String login(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("usuario", authentication.getName());
        } else {
            model.addAttribute("usuario", null);
        }
        return "login";
    }

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
     */

    @GetMapping("/coleccion/example")
    public String showColeccionExample() {
        return "commons/showColeccionExample";
    }
}
