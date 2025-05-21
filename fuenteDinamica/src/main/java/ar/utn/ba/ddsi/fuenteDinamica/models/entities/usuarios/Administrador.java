package ar.utn.ba.ddsi.fuenteDinamica.models.entities.usuarios;

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

    public Boolean esAdministrador() {return true;}


    @Override
    public Long getId() {
        return id;
    }
}
