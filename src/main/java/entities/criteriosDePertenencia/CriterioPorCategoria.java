package entities.criteriosDePertenencia;

import entities.hechos.Hecho;

public class CriterioPorCategoria implements CriterioDePertenencia{
    private final String categoria;

    public CriterioPorCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getDatosHechos().getCategoria().equalsIgnoreCase(categoria);
    }
}
