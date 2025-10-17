package com.frontMetaMapa.frontMetaMapa.models.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HechoInputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;

    private String latitud;
    private String longitud;
    private String localidad;
    private Long provincia;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaHecho;

    private List<MultipartFile> multimedia;
    private Boolean mostrarDatos = false;
}
