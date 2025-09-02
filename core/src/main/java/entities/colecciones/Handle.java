package entities.colecciones;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Getter
@Embeddable
public class Handle {
    @Column(name = "handle")
    private String value;

    public Handle() {
        // Genera un UUID y lo convierte a un string alfanumérico (sin guiones)
        this.value = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public Handle(long value){
        this.value = String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Handle handle)) return false;
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
