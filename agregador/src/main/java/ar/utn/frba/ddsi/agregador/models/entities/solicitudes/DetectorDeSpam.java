package ar.utn.frba.ddsi.agregador.models.entities.solicitudes;
import java.util.*;



public class DetectorDeSpam implements IDetectorDeSpam {

    // Términos de spam comunes con sus pesos IDF (Inverse Document Frequency)
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

    private static final double SPAM_THRESHOLD = 5.0; // Umbral para considerar spam

    public boolean isSpam(String texto) {
        // Preprocesamiento del texto
        String cleanedText = texto.toLowerCase()
                .replaceAll("[^a-záéíóúüñ]", " ");

        // Tokenización
        List<String> tokens = Arrays.asList(cleanedText.split("\\s+"));

        // Calcular frecuencia de términos (TF)
        Map<String, Integer> termFrequency = new HashMap<>();
        for (String token : tokens) {
            termFrequency.put(token, termFrequency.getOrDefault(token, 0) + 1);
        }

        // Calcular puntuación TF-IDF para términos de spam
        double spamScore = 0.0;
        for (Map.Entry<String, Double> entry : SPAM_TERMS_IDF.entrySet()) {
            String term = entry.getKey();
            double idf = entry.getValue();
            int tf = termFrequency.getOrDefault(term, 0);

            spamScore += tf * idf;
        }

        // Normalizar por longitud del texto
        spamScore = spamScore * 500 / texto.length();

        return spamScore > SPAM_THRESHOLD;
    }
}