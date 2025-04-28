package entities.hechos;

import entities.colecciones.Coleccion;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hecho {
    private String autor;
    private Boolean esValido;
    private DatosHechos datosHechos;
    private Multimedia multimedia;
    private List<String> etiquetas;
    private List<Coleccion> colecciones;
    private Boolean mostrarDatos;

    public static Hecho create(DatosHechos datosHechos, String autor) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .autor(autor)
                .etiquetas(new ArrayList<>())
                .colecciones(new ArrayList<>())
                .build();
    }

    public static Hecho create(DatosHechos datosHechos){
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .colecciones(new ArrayList<>())
                .build();
    }

    public static Hecho create(DatosHechos datosHechos, String autor, Multimedia multimedia) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .autor(autor)
                .multimedia(multimedia)
                .etiquetas(new ArrayList<>())
                .colecciones(new ArrayList<>())
                .build();

    }

    public void addEtiqueta(String etiqueta) {
            this.etiquetas.add(etiqueta);
    }

    public void addColeccion(Coleccion coleccion) {
        this.colecciones.add(coleccion);
    }

}
