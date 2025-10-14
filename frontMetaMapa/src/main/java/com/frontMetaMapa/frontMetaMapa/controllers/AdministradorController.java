package com.frontMetaMapa.frontMetaMapa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdministradorController {
    @GetMapping("/administrador")
    public String administrador() {
        return "administrador/index";
    }

    @GetMapping("/dashboard-solicitudes")
    public String solicitudes() {
        return "administrador/dashboardSolicitud";
    }

    @GetMapping("/panel-control")
    public String panelControl() {
        return "administrador/panelControl";
    }

    @GetMapping("/mis-colecciones")
    public String misColecciones() {
        return "administrador/verColecciones";
    }

    @GetMapping("/subirCsv")
    public String subirCsv() {
        return "administrador/uploadCSV";
    }
}
