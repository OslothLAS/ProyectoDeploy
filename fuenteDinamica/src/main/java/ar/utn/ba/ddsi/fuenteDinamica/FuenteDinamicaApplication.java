package ar.utn.ba.ddsi.fuenteDinamica;

import ar.utn.ba.ddsi.fuenteDinamica.config.HechoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableConfigurationProperties(HechoProperties.class)
public class FuenteDinamicaApplication {

    @Autowired
    private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(FuenteDinamicaApplication.class, args);
        System.out.println("hola mundo ");
    }

    /*@PostConstruct
    public void insertarProvincia() {
        jdbcTemplate.execute("INSERT INTO provincia (nombre) VALUES ('Buenos Aires')");
        System.out.println("✅ Provincia 'Buenos Aires' insertada");
    }*/

}
