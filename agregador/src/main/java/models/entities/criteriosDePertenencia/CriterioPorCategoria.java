package models.entities.criteriosDePertenencia;

import models.entities.hechos.Hecho;

import static utils.NormalizadorTexto.normalizarTexto;

public class CriterioPorCategoria implements CriterioDePertenencia{
    private final String categoria;

    public CriterioPorCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return normalizarTexto(hecho.getDatosHechos().getCategoria()).contains(normalizarTexto(categoria));
    }
}
