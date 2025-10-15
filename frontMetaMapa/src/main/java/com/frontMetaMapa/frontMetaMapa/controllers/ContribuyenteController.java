package com.frontMetaMapa.frontMetaMapa.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContribuyenteController {
    @GetMapping("/contribuyente")
    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    public String contribuyente() {
        return "contribuyente/index";
    }

    @GetMapping ("/subir-hecho")
    public String subirHecho(){
        return "contribuyente/subirHecho";
        }

    @GetMapping("/mis-contribuciones")
    public String misContribuciones(){
        return "contribuyente/misContribuciones";
    }

    @GetMapping("/solicitud-eliminacion")
    public String solicitudEliminacion(){
        return "contribuyente/solicitudEliminacion";
    }
}
