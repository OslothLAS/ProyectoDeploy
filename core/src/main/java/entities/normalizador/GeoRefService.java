package entities.normalizador;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;


public class GeoRefService {


    public static List<String> obtenerUbicacion(double latitud, double longitud) {
        List<String> resultado = new ArrayList<>();

        try {
            String urlString = String.format(
                    Locale.US,
                    "https://apis.datos.gob.ar/georef/api/ubicacion?lat=%.6f&lon=%.6f",
                    latitud, longitud
            );

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Java-GeorefClient/1.0");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());

                if (jsonResponse.has("ubicacion")) {
                    JSONObject ubicacion = jsonResponse.getJSONObject("ubicacion");

                    // Extraer provincia
                    String provincia = ubicacion.optString("provincia", "Desconocido");

                    // Extraer departamento (similar a ciudad/municipio en Argentina)
                    String ciudad = ubicacion.optString("ciudad", "Desconocido");

                    // Extraer localidad (ciudad específica)
                    String localidad = ubicacion.optString("localidad", "Desconocido");

                    resultado.add(provincia);
                    resultado.add(ciudad);
                    resultado.add(localidad);
                }
            }

            connection.disconnect();

        } catch (Exception e) {
            System.err.println("Error al consultar Georef: " + e.getMessage());
        }

        return resultado;
    }


}
