package ar.utn.ba.ddsi.fuenteDinamica;

import ar.utn.ba.ddsi.fuenteDinamica.config.HechoProperties;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void insertarProvincias() {
        String sql = """
            INSERT IGNORE INTO provincia (nombre) VALUES
            ('Buenos Aires'),
            ('Catamarca'),
            ('Chaco'),
            ('Chubut'),
            ('Cordoba'),
            ('Corrientes'),
            ('Entre Rios'),
            ('Formosa'),
            ('Jujuy'),
            ('La Pampa'),
            ('La Rioja'),
            ('Mendoza'),
            ('Misiones'),
            ('Neuquen'),
            ('Rio Negro'),
            ('Salta'),
            ('San Juan'),
            ('San Luis'),
            ('Santa Cruz'),
            ('Santa Fe'),
            ('Santiago del Estero'),
            ('Tierra del Fuego'),
            ('Tucuman');
        """;

        jdbcTemplate.execute(sql);
        System.out.println("Provincias insertadas");
    }

}
