package ar.utn.frba.ddsi.agregador.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "fuentes")
@Primary
@Getter
@Setter
public class FuentesProperties {
    private List<FuenteConfig> fuentes  = new ArrayList<>();
}
