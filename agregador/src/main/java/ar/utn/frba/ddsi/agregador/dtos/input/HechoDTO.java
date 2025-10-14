package ar.utn.frba.ddsi.agregador.dtos.input;


import ar.utn.frba.ddsi.agregador.models.entities.hechos.Multimedia;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Origen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HechoDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaHecho;
    private List<Multimedia> multimedia;

    private Origen origen;
    private Boolean mostrarDatos;
    private Boolean esValido;
    private LocalDateTime fechaCreacion;
}
