package ar.utn.frba.ddsi.agregador.models.entities.normalizador;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

public class OpenStreetMap {

    public static List<String> obtenerUbicacion(double latitud, double longitud) {
        List<String> resultado = new ArrayList<>();

        try {

            String urlString = String.format(
                    Locale.US,
                    "https://nominatim.openstreetmap.org/reverse?format=json&lat=%.6f&lon=%.6f&zoom=18&addressdetails=1",
                    latitud, longitud
            );

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "MiApp/1.0 osilveroruiz@frba.utn.edu.ar");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

            if (connection.getResponseCode() == 200) {

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }



                    JSONObject jsonResponse = new JSONObject(response.toString());

                    if (jsonResponse.has("address")) {
                        JSONObject address = jsonResponse.getJSONObject("address");

                        String provincia = address.optString("state", "");
                        String ciudad = address.optString("city",
                                address.optString("town",
                                        address.optString("village",
                                                address.optString("municipality",
                                                        address.optString("county",
                                                                address.optString("region", "Desconocido"))))));

                        if (!provincia.isEmpty()) resultado.add(provincia);
                        if (!ciudad.isEmpty()) resultado.add(ciudad);
                    }
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }
}