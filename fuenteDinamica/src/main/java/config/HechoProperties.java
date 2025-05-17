package config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hecho")
public class HechoProperties {
    @Getter
    @Setter
    private int plazoEdicionDias;

}