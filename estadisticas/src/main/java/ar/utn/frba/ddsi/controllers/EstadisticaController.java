package ar.utn.frba.ddsi.controllers;

import ar.utn.frba.ddsi.dtos.StatDTO;
import ar.utn.frba.ddsi.services.IEstadisticaService;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticaController {
    private IEstadisticaService estadisticaService;

    public EstadisticaController(IEstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping("/{idColeccion}")
    public List<StatDTO> calcularProvinciaPorHechos(@PathVariable(name = "idColeccion") Long idColeccion){
        return estadisticaService.calcularProvinciaPorHechos(idColeccion);
    }

    @GetMapping("/categoria-hechos")
    public List<StatDTO> calcularCategoriaPorHechos(){
        return estadisticaService.calcularCategoriaPorHechos();
    }

    @GetMapping("/{idCategoria}")
    public List<StatDTO> calcularProvinciaMasReportada(@PathVariable(name = "idCategoria") Long idCategoria){
        return this.estadisticaService.calcularMaxHechos(idCategoria);
    }

    @GetMapping("/hora-top")
    public List<StatDTO> calcularHoraPico(@PathVariable(name = "idCategoria") Long idCategoria){
        return this.estadisticaService.calcularHoraPico(idCategoria);
    }

    @GetMapping("/solicitudes-spam")
    public List<StatDTO> calcularSolicitudesPorSpam(){
        return this.estadisticaService.calcularSolicitudesPorSpam();
    }

    @GetMapping("/exportar-csv")
    public void exportarCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"estadisticas.csv\"");

        List<StatDTO> stats = estadisticaService.generateCSV();

        try (PrintWriter writer = response.getWriter();
             CSVWriter csvWriter = new CSVWriter(writer)) {

            // Cabecera del CSV
            String[] header = { "Descripcion", "Cantidad" };
            csvWriter.writeNext(header);

            // Datos
            for (StatDTO stat : stats) {
                String[] line = {
                        stat.getDescripcion() != null ? stat.getDescripcion() : "",
                        stat.getCantidad() != null ? stat.getCantidad().toString() : "",
                };
                csvWriter.writeNext(line);
            }

            csvWriter.flush();
        }
    }

    @PutMapping
    public void calcular(){
        this.estadisticaService.calcularEstadisticas();
    }
}
