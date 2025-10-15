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
    private final IEstadisticaService estadisticaService;

    public EstadisticaController(IEstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }
/* DONE De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?*/
    @GetMapping("/prov-top-reportada/{nombreColeccion}")
    public StatDTO getProvinciaMasReportadaPorColeccion(@PathVariable(name = "nombreColeccion") String nombreColeccion){
        return estadisticaService.getProvinciaMasReportadaPorColeccion(nombreColeccion);
    }

    /*@GetMapping("/provinciasxcategorias")
    public List<StatDTO> provinciasPorCategorias(){
        return estadisticaService.calcularProvinciaPorCategorias();
    }*/

/* DONE ¿Cuál es la categoría con mayor cantidad de hechos reportados?*/
    @GetMapping("/categoria-hechos")
    public StatDTO calcularCategoriaPorHechos(){
        return estadisticaService.getCategoriaConMasHechos();
    }

    @GetMapping("/{idCategoria}")
    public StatDTO calcularProvinciaMasReportada(@PathVariable(name = "idCategoria") String categoria){
        return this.estadisticaService.getProvinciaConMasHechosDeCategoria(categoria);
    }

    @GetMapping("/hora-top/categoria")
    public StatDTO calcularHoraPico(@PathVariable(name = "categoria") String categoria){
        return this.estadisticaService.getHoraPicoDeCategoria(categoria);
    }

    @GetMapping("/solicitudes-spam")
    public StatDTO calcularSolicitudesPorSpam(){
        return this.estadisticaService.getCantidadDeSpam();
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
                        stat.getDescripcion() != null ? stat.getDescripcion().name() : "",
                        stat.getCantidad() != null ? stat.getCantidad().toString() : "",
                };
                csvWriter.writeNext(line);
            }

            csvWriter.flush();
        }
    }
    @GetMapping
    public List<StatDTO> findAll(){
        return this.estadisticaService.findAll();
    }
    @PutMapping
    public void calcular(){
        this.estadisticaService.calcularEstadisticas();
    }
}
