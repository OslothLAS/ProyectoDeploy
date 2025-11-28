package com.frontMetaMapa.frontMetaMapa.models.dtos.Api;

import com.frontMetaMapa.frontMetaMapa.models.dtos.input.LocalidadDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UbicacionInput {
    private String latitud;
    private String longitud;

    private Localidad localidad;

    // Constructor que normaliza automáticamente
    public UbicacionInput(String latitud, String longitud, Localidad localidad) {
        this.latitud = normalizeCoordinate(latitud);
        this.longitud = normalizeCoordinate(longitud);
        this.localidad = localidad;
    }

    // Setter personalizado para latitud
    public void setLatitud(String latitud) {
        this.latitud = normalizeCoordinate(latitud);
    }

    // Setter personalizado para longitud
    public void setLongitud(String longitud) {
        this.longitud = normalizeCoordinate(longitud);
    }

    private String normalizeCoordinate(String valor) {
        if (valor != null && !valor.isBlank()) {
            // reemplaza coma por punto
            String valorNormalizado = valor.replace(',', '.');
            double parsed = Double.parseDouble(valorNormalizado);
            return String.format("%.4f", parsed);
        }
        return valor;
    }
}
