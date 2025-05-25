package models.entities.solicitudes;
import services.IDetectorDeSpam;
import utils.ConfigLoader;

import java.util.*;
import java.util.stream.Collectors;


public class DetectorDeSpam implements IDetectorDeSpam {

        private final Map<String, Double> spamTermsIdf;
        private final double spamThreshold;

        public DetectorDeSpam() {
            this.spamTermsIdf = ConfigLoader.getSpamTermsIdf(); // Términos de spam comunes con sus pesos IDF (Inverse Document Frequency)
            this.spamThreshold = ConfigLoader.getSpamThreshold(); // Umbral para considerar spam
        }

        @Override
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
            for (Map.Entry<String, Double> entry : this.spamTermsIdf.entrySet()) {
                String term = entry.getKey();
                double idf = entry.getValue();
                int tf = termFrequency.getOrDefault(term, 0);

                spamScore += tf * idf;
            }

            // Normalizar por longitud del texto
            spamScore = spamScore * 500 / texto.length();

            return spamScore > this.spamThreshold;
        }
}
