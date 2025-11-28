package ar.utn.ba.ddsi.fuenteDinamica.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UbicacionDTO {

    private String latitud;
    private String longitud;
    private LocalidadDTO localidad;

    // Constructor que normaliza automáticamente
    public UbicacionDTO(String latitud, String longitud, LocalidadDTO localidad) {
        this.latitud = normalizeCoordinate(latitud);
        this.longitud = normalizeCoordinate(longitud);
        this.localidad = localidad;
    }

    // Setters personalizados que normalizan
    public void setLatitud(String latitud) {
        this.latitud = normalizeCoordinate(latitud);
    }

    public void setLongitud(String longitud) {
        this.longitud = normalizeCoordinate(longitud);
    }

    private String normalizeCoordinate(String valor) {
        if (valor != null && !valor.isBlank()) {
            return valor.replace(',', '.');
        }
        return valor;
    }
}
