package com.frontMetaMapa.frontMetaMapa.models.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HechoInputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;

    // Campos planos desde el form
    private String latitud;
    private String longitud;
    private String localidad;
    private Long provincia;

    private LocalDateTime fechaHecho;

    // Archivos multimedia del input type="file"
    private List<MultipartFile> multimedia;

    private Boolean mostrarDatos = false;
}
