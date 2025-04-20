package entities.colecciones;

import entities.hechos.Hecho;

import java.time.LocalDate;

public class CriterioPertenencia {
    private String nombre;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private boolean cumpleConCriterio(Hecho hecho){
        //TODO
        return true;
    }
}