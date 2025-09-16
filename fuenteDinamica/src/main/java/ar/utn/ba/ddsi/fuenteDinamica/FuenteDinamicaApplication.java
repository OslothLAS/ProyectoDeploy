package ar.utn.ba.ddsi.fuenteDinamica;

import config.HechoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(HechoProperties.class)
@EntityScan("entities")
public class FuenteDinamicaApplication {

	public static void main(String[] args) {

		SpringApplication.run(FuenteDinamicaApplication.class, args);
		System.out.println("hola mundo ");
	}
}
