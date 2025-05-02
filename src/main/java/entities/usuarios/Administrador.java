package entities.usuarios;

public class Administrador implements Usuario {
    private Integer id;
    private String nombre;

    @Override
    public String getNombre() {
        return nombre;
    }
}
