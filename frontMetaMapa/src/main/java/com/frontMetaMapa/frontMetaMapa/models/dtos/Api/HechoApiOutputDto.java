package com.frontMetaMapa.frontMetaMapa.models.dtos.Api;

import com.frontMetaMapa.frontMetaMapa.models.dtos.input.MultimediaDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.UbicacionDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter

public class HechoApiOutputDto {

        private Long id;
        private String titulo;
        private String descripcion;
        private String username;
        private String categoria;
        private UbicacionDTO ubicacion;
        private LocalDateTime fechaHecho;
        private List<MultimediaDTO> multimedia;
        private String origen;
        private Boolean mostrarDatos;
        private Boolean esValido;
        private LocalDateTime fechaCreacion;

        // Método para verificar si el hecho es editable (dentro de los primeros 7 días)
        public boolean isEditable() {
                if (fechaCreacion == null) {
                        return false;
                }
                long daysBetween = ChronoUnit.DAYS.between(fechaCreacion, LocalDateTime.now());
                return daysBetween <= 7;
        }
}