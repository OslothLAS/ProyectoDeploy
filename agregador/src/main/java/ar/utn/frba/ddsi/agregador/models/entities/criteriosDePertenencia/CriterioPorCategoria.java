package ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;

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
