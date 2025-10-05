package ar.utn.frba.ddsi.agregador.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HechoOutputDTO {
    private Long id;
    private String username;
    private Boolean esValido;
    private List<String> multimedia;
    private List<String> etiquetas = new ArrayList<>();
    //private List<Coleccion> colecciones = new ArrayList<>();
    private List<Handle> handles = new ArrayList<>();
    private Origen origen;
    private FuenteOrigen fuenteOrigen;
    private Boolean mostrarDatos; //ver esto
    private LocalDateTime fechaCreacion;
    private Duration plazoEdicion;
    private Boolean esEditable;

    //datos
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaHecho;
}
