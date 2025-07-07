package entities.usuarios;

import lombok.Getter;

@Getter
public class Administrador implements Usuario {
    private Long id;
    private String nombre;

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public Boolean getRegistrado() {
        return true;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
