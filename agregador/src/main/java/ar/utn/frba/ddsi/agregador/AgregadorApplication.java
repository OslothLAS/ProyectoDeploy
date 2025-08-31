package ar.utn.frba.ddsi.agregador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "entities")
public class AgregadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgregadorApplication.class, args);
	}

}
