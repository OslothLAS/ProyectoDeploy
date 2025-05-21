package ar.utn.ba.ddsi.fuenteDinamica.models.entities.usuarios;

public interface Usuario {
        String getNombre();
        Boolean getRegistrado();
        Boolean esAdministrador();

        Long getId();
}

