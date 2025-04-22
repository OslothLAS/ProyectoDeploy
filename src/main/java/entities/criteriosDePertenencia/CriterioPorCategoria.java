package entities.criteriosDePertenencia;

import entities.hechos.Hecho;

import static utils.ExtensionReader.normalizarTexto;

public class CriterioPorCategoria implements CriterioDePertenencia{
    private final String categoria;

    public CriterioPorCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return normalizarTexto(hecho.getDatosHechos().getCategoria()).equals(normalizarTexto(categoria));    }
}
