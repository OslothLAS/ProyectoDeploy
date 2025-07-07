package entities.hechos;

import entities.colecciones.Handle;
import entities.usuarios.Usuario;
import entities.usuarios.Visualizador;
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
    private Long id;
    private String autor;
    private Usuario usuario;
    private Boolean esValido;
    private DatosHechos datosHechos;
    private Multimedia multimedia;
    @Builder.Default
    private List<String> etiquetas = new ArrayList<>();
    @Builder.Default
    private List<Handle> colecciones = new ArrayList<>();
    private Origen origen;
    private FuenteOrigen fuenteOrigen;
    private Boolean mostrarDatos;

    private LocalDateTime fechaCreacion;
    private Duration plazoEdicion;
    private Boolean esEditable;
    private Boolean esConsensuado;

    public static Hecho create(DatosHechos datosHechos){
        return Hecho.builder()
                .datosHechos(datosHechos)
                .usuario(null)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .origen(Origen.DATASET)
                .colecciones(new ArrayList<>())
                .esConsensuado(false)
                .build();
    }

    public static Hecho create(DatosHechos datosHechos, List<Handle> colecciones,Long id,Boolean esConsensuado) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .id(id)
                .usuario(null)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .origen(Origen.EXTERNO)
                .colecciones(colecciones)
                .esConsensuado(esConsensuado)
                .build();
    }


    public static Hecho create(DatosHechos datosHechos, Visualizador visualizador) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .usuario(visualizador)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCreacion(LocalDateTime.now())
                .origen(Origen.VISUALIZADOR)
                .colecciones(new ArrayList<>())
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
                .colecciones(new ArrayList<>())
                .esConsensuado(false)
                .build();
    }
    //creacion con multimedia registrado (multimedia puede ser null tranquilamente)
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
                .origen(Origen.CONTRIBUYENTE)
                .colecciones(new ArrayList<>())
                .esConsensuado(false)
                .build();
    }


    public void addEtiqueta(String etiqueta) {
            this.etiquetas.add(etiqueta);
    }

    public void addColeccion(Handle coleccion) {
        if (this.colecciones == null) {
            this.colecciones = new ArrayList<>();
        }
        this.colecciones.add(coleccion);
    }

    public Boolean esEditable() {
        if (!this.esEditable) {
            return false;
        }
        LocalDateTime fechaLimite = this.fechaCreacion.plus(this.plazoEdicion);
        return LocalDateTime.now().isBefore(fechaLimite);
    }

    public List<String> getTituloYDescripcion(){
        String titulo = this.getDatosHechos().getTitulo();
        String descripcion = this.getDatosHechos().getDescripcion();
        List<String> tituloYdesc = new ArrayList<>();
        tituloYdesc.add(titulo);
        tituloYdesc.add(descripcion);

        return tituloYdesc;
    }
}
