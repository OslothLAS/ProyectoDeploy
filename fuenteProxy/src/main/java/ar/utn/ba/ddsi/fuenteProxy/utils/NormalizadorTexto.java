package ar.utn.ba.ddsi.fuenteProxy.utils;

import java.text.Normalizer;

public class NormalizadorTexto {
    public static String normalizarTexto(String texto) {
        // SACA TILDES Y MAYUSCULAS
        String textoSinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoSinTildes.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

    public static String normalizarTrimTexto(String texto) {
        String textoSinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD).trim();
        return textoSinTildes
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("\\s+", "")
                .toLowerCase();
    }
}
