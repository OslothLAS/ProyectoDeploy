package ar.utn.frba.ddsi.agregador.models.entities.solicitudes;

import java.util.*;
import java.util.regex.Pattern;

public class DetectorDeSpam implements IDetectorDeSpam {

    private static final DetectorDeSpam INSTANCE = new DetectorDeSpam();

    // --- Configuración TF-IDF ---
    private static final Map<String, Double> SPAM_TERMS_IDF = Map.ofEntries(
            Map.entry("oferta", 2.1),
            Map.entry("gratis", 2.3),
            Map.entry("ganador", 2.5),
            Map.entry("urgente", 2.2),
            Map.entry("dinero", 2.0),
            Map.entry("click", 2.4),
            Map.entry("aquí", 1.8),
            Map.entry("promoción", 2.3),
            Map.entry("exclusivo", 2.1),
            Map.entry("limitado", 2.2),
            Map.entry("prueba", 1.9),
            Map.entry("seguro", 1.7),
            Map.entry("crédito", 2.0),
            Map.entry("préstamo", 2.1),
            Map.entry("millón", 2.5),
            Map.entry("dólar", 2.0),
            Map.entry("bitcoin", 2.4),
            Map.entry("ganar", 2.3),
            Map.entry("sorteo", 2.6),
            Map.entry("visita", 1.8)
    );
    private static final double SPAM_THRESHOLD = 5.0;

    // --- Configuración Heurística (Gibberish) ---
    private static final int MAX_WORD_LENGTH = 23; // "electroencefalografista" tiene 23
    private static final int MAX_CONSECUTIVE_CONSONANTS = 5; // "a-bstr-acción" tiene 4
    private static final Pattern CONSONANT_PATTERN = Pattern.compile("[bcdfghjklmnñpqrstvwxyz]{5,}");


    public boolean isSpam(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;

        // 1. Chequeo Rápido: ¿Parece basura (gibberish)?
        if (isGibberish(texto)) {
            System.out.println("Detectado como spam por estructura (gibberish): " + texto);
            return true;
        }

        // 2. Chequeo Semántico: TF-IDF (Tu lógica original)
        return calculateTfIdfSpamScore(texto) > SPAM_THRESHOLD;
    }

    /**
     * Detecta patrones de escritura sin sentido (teclazo aleatorio).
     */
    private boolean isGibberish(String texto) {
        String[] words = texto.trim().split("\\s+");

        int totalChars = 0;
        int vowelCount = 0;

        for (String word : words) {
            // Regla A: Palabra excesivamente larga sin espacios
            if (word.length() > MAX_WORD_LENGTH) {
                return true;
            }

            // Regla B: Demasiadas consonantes seguidas
            // Normalizamos para quitar acentos antes de checkear regex, pero mantenemos la ñ
            String normalizedWord = word.toLowerCase();
            if (CONSONANT_PATTERN.matcher(normalizedWord).find()) {
                return true;
            }

            // Contar para ratio de vocales
            for (char c : normalizedWord.toCharArray()) {
                if (Character.isLetter(c)) {
                    totalChars++;
                    if ("aeiouáéíóúü".indexOf(c) >= 0) {
                        vowelCount++;
                    }
                }
            }
        }

        // Regla C: Ratio de vocales muy bajo (ej: "hklmn tqrst")
        // Evitamos dividir por cero. Si es muy corto, ignoramos esta regla.
        if (totalChars > 5) {
            double vowelRatio = (double) vowelCount / totalChars;
            // Si menos del 15% son vocales, es sospechoso
            if (vowelRatio < 0.15) {
                return true;
            }
        }

        return false;
    }

    private double calculateTfIdfSpamScore(String texto) {
        // Tu lógica original encapsulada aquí para mantener el código limpio
        String cleanedText = texto.toLowerCase().replaceAll("[^a-záéíóúüñ]", " ");
        List<String> tokens = Arrays.asList(cleanedText.split("\\s+"));

        Map<String, Integer> termFrequency = new HashMap<>();
        for (String token : tokens) {
            termFrequency.put(token, termFrequency.getOrDefault(token, 0) + 1);
        }

        double spamScore = 0.0;
        for (Map.Entry<String, Double> entry : SPAM_TERMS_IDF.entrySet()) {
            String term = entry.getKey();
            double idf = entry.getValue();
            int tf = termFrequency.getOrDefault(term, 0);
            spamScore += tf * idf;
        }

        if (texto.length() > 0) {
            spamScore = spamScore * 500 / texto.length();
        }

        return spamScore;
    }

    public static DetectorDeSpam getInstance() {
        return INSTANCE;
    }
}