package com.frontMetaMapa.frontMetaMapa.models.DTOS.input;


import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.FuenteOrigen;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.Origen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.asm.Handle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HechoInputDTO {
    private AutorDTO autor;
    private Boolean esValido;
    private List<String> multimedia;
    private List<String> etiquetas = new ArrayList<>();
    private List<ColeccionInputDTO> colecciones = new ArrayList<>();
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
    private CategoriaDTO categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaHecho;
}
