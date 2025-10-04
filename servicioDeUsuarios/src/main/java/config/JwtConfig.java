/*package config;

import com.usuarios.servicioDeUsuarios.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret.key}") // Clave cargada del application.yml
    private String jwtSecret;

    @PostConstruct
    public void init() {
        JwtUtil.init(jwtSecret); // Inicializa la clave en el JwtUtil estático
    }
}*/
