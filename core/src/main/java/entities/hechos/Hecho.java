package entities.hechos;

import entities.colecciones.Handle;
import entities.usuarios.Usuario;
import entities.usuarios.Visualizador;
import lombok.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hecho {
    private Long id;

    private DatosHechos datosHechos;
    /*borrar!! (mucho quilombo)

    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaHecho;
    */


    private String autor; //?
    private Map<Handle,Boolean> colecciones;
    private Usuario usuario;
    private List<String> etiquetas = new ArrayList<>();
    private Boolean mostrarDatos;
    private Duration plazoEdicion;
    private LocalDateTime fechaCarga;
    private Origen origenCarga;
    private FuenteOrigen fuenteOrigen;
    private List<Multimedia> multimedia;
    private Boolean esEditable;
    private Boolean esValido;

    public static Hecho create(DatosHechos datosHechos){
        return Hecho.builder()
                .datosHechos(datosHechos)
                .usuario(null)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCarga(LocalDateTime.now())
                .origenCarga(Origen.DATASET)
                .colecciones(new HashMap<>())
                .build();
    }

    public static Hecho create(DatosHechos datosHechos, Map<Handle,Boolean> colecciones,Long id) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .id(id)
                .usuario(null)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCarga(LocalDateTime.now())
                .origenCarga(Origen.EXTERNO)
                .colecciones(colecciones)
                .build();
    }


    public static Hecho create(DatosHechos datosHechos, Visualizador visualizador) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .usuario(visualizador)
                .esValido(true)
                .etiquetas(new ArrayList<>())
                .fechaCarga(LocalDateTime.now())
                .origenCarga(Origen.VISUALIZADOR)
                .colecciones(new HashMap<>())
                .build();
    }

    //creacion con multimedia anonima
    public static Hecho create(DatosHechos datosHechos, List<Multimedia> multimedia) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .multimedia(multimedia)
                .etiquetas(new ArrayList<>())
                .fechaCarga(LocalDateTime.now())
                .colecciones(new HashMap<>())
                .build();
    }
    //creacion con multimedia registrado (multimedia puede ser null tranquilamente)
    public static Hecho create(DatosHechos datosHechos, Usuario usuario, List<Multimedia> multimedia, Boolean mostrarDatos) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .autor(usuario.getNombre())
                .usuario(usuario)
                .multimedia(multimedia)
                .etiquetas(new ArrayList<>())
                .mostrarDatos(mostrarDatos)
                .fechaCarga(LocalDateTime.now())
                .origenCarga(Origen.CONTRIBUYENTE)
                .colecciones(new HashMap<>())
                .build();
    }


    public void addEtiqueta(String etiqueta) {
            this.etiquetas.add(etiqueta);
    }

    public void addColeccion(Handle coleccion) {
        if (this.colecciones == null) {
            this.colecciones = new HashMap<>();
        }
        this.colecciones.put(coleccion, Boolean.TRUE);
    }

    public Boolean esEditable() {
        if (!this.esEditable) {
            return false;
        }
        LocalDateTime fechaLimite = this.fechaCarga.plus(this.plazoEdicion);
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
