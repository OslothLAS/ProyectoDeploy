package utils;

public class ExtensionReader {
    public static String getFileExtension(String nombreArchivo) {
        int lastDot = nombreArchivo.lastIndexOf('.');
        if (lastDot == -1) {
            return ""; // Sin extensión
        }
        return nombreArchivo.substring(lastDot + 1);
    }
}
