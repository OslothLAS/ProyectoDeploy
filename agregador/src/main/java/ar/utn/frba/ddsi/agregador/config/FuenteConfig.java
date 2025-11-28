package ar.utn.frba.ddsi.agregador.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FuenteConfig {
    private String ip;
    private String puerto;
}
