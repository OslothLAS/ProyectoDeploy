package models.entities.usuarios;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
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

}
