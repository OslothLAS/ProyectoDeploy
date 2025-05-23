package models.entities.criteriosDePertenencia;

import models.entities.hechos.Hecho;

import java.time.LocalDate;

public class CriterioPorFecha implements CriterioDePertenencia{
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    public CriterioPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        LocalDate fechaHecho = hecho.getDatosHechos().getFechaHecho();
        return !fechaHecho.isBefore(fechaInicio) && !fechaHecho.isAfter(fechaFin);
    }
}
