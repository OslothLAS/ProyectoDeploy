package utils;

import java.text.Normalizer;

public class ExtensionReader {
    public static String getFileExtension(String nombreArchivo) {
        int lastDot = nombreArchivo.lastIndexOf('.');
        if (lastDot == -1) {
            return ""; // Sin extensión
        }
        return nombreArchivo.substring(lastDot + 1);
    }

    public static String normalizarTexto(String texto) {
        // SACA TILDES Y MAYUSCULAS
        String textoSinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoSinTildes.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }
}



