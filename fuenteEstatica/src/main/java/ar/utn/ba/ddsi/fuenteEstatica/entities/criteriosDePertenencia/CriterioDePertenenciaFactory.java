package ar.utn.ba.ddsi.fuenteEstatica.entities.criteriosDePertenencia;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CriterioDePertenenciaFactory {

    public static List<CriterioDePertenencia> crearCriterios(Map<String, String> filtros) {
        List<CriterioDePertenencia> criterios = new ArrayList<>();

        if (filtros.containsKey("fechaInicio") && filtros.containsKey("fechaFin")) {
            try {
                LocalDateTime fechaInicio = LocalDateTime.parse(filtros.get("fechaInicio"));
                LocalDateTime fechaFin = LocalDateTime.parse(filtros.get("fechaFin"));
                criterios.add(new CriterioPorFecha(fechaInicio, fechaFin));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Formato de fecha inválido. Use formato ISO (yyyy-MM-dd)");
            }
        }

        if (filtros.containsKey("categoria")) {
            String categoria = filtros.get("categoria");
            if (categoria != null && !categoria.trim().isEmpty()) {
                criterios.add(new CriterioPorCategoria(categoria));
            }
        }

        return criterios;
    }
}
