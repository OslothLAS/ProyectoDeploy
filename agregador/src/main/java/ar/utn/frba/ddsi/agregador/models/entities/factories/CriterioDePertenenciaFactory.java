package ar.utn.frba.ddsi.agregador.models.entities.factories;

import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioPorCategoria;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioPorFecha;

import java.time.LocalDate;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CriterioDePertenenciaFactory {

    public static List<CriterioDePertenencia> crearCriterios(Map<String, String> filtros) {
        List<CriterioDePertenencia> criterios = new ArrayList<>();

        if (filtros.containsKey("fechaInicio") || filtros.containsKey("fechaFin")) {
            try {
                LocalDate fechaInicio = null;
                LocalDate fechaFin = null;

                if (filtros.containsKey("fechaInicio") && filtros.get("fechaInicio") != null && !filtros.get("fechaInicio").trim().isEmpty()) {
                    fechaInicio = LocalDate.parse(filtros.get("fechaInicio"));
                }
                if (filtros.containsKey("fechaFin") && filtros.get("fechaFin") != null && !filtros.get("fechaFin").trim().isEmpty()) {
                    fechaFin = LocalDate.parse(filtros.get("fechaFin"));
                }

                if (fechaInicio != null || fechaFin != null) {
                    criterios.add(new CriterioPorFecha(fechaInicio, fechaFin));
                }
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
