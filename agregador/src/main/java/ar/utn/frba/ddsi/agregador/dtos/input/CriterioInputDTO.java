package ar.utn.frba.ddsi.agregador.dtos.input;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CriterioInputDTO {
    private String tipo;             // "categoria" o "fecha"
    // Solo si tipo == "categoria"
    private String categoria;
    // Solo si tipo == "fecha"
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
