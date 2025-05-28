package ar.utn.frba.ddsi.agregador.models.entities.colecciones;

import java.util.UUID;

public class Handle {
    private final String value;

    public Handle() {
        // Genera un UUID y lo convierte a un string alfanumérico (sin guiones)
        this.value = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public Handle(String value) {
        // Si necesitas crear un handle a partir de un string dado
        if (!value.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("El handle debe ser alfanumérico y sin espacios.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Handle)) return false;
        Handle handle = (Handle) o;
        return value.equals(handle.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
