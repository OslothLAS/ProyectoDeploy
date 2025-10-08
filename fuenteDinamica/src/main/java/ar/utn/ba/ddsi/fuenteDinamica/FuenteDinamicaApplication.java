package ar.utn.ba.ddsi.fuenteDinamica;

import ar.utn.ba.ddsi.fuenteDinamica.config.HechoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableConfigurationProperties(HechoProperties.class)
public class FuenteDinamicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuenteDinamicaApplication.class, args);
        System.out.println("hola mundo ");
    }
}
