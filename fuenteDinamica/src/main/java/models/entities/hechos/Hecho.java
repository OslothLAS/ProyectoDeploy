package models.entities.hechos;


import models.entities.usuarios.Usuario;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hecho {
    //autor es usuario.getNombre()
    private String autor;
    private Usuario usuario;
    private Boolean esValido;
    private DatosHechos datosHechos;
    private Multimedia multimedia;
    private List<String> etiquetas;
    private Boolean mostrarDatos;
    private LocalDateTime fechaCreacion;
    private Duration plazoEdicion;
    private Boolean esEditable;

    public Boolean estaRegistrado(){
        return this.usuario.getRegistrado();
    }
//creacion anonima osea no tiene usuario
    public static Hecho create(DatosHechos datosHechos) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .build();
    }
    //creacion de forma registrado
    public static Hecho create(DatosHechos datosHechos, Usuario usuario, Boolean mostrarDatos) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .autor(usuario.getNombre())
                .usuario(usuario)
                .etiquetas(new ArrayList<>())
                .mostrarDatos(mostrarDatos)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }
    //creacion con multimedia anonima
    public static Hecho create(DatosHechos datosHechos, Multimedia multimedia) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .multimedia(multimedia)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .build();
    }
//creacion con multimedia registrado
    public static Hecho create(DatosHechos datosHechos, Usuario usuario, Multimedia multimedia, Boolean mostrarDatos) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .autor(usuario.getNombre())
                .usuario(usuario)
                .multimedia(multimedia)
                .etiquetas(new ArrayList<>())
                .mostrarDatos(mostrarDatos)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }


    public void addEtiqueta(String etiqueta) {
            this.etiquetas.add(etiqueta);
    }

    public Boolean esEditable() {
        if (!this.esEditable) {
            return false;
        }
        LocalDateTime fechaLimite = this.fechaCreacion.plus(this.plazoEdicion);
        return LocalDateTime.now().isBefore(fechaLimite);
    }

}
