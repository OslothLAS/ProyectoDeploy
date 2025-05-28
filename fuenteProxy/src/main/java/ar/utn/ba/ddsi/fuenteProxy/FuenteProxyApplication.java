package ar.utn.ba.ddsi.fuenteProxy;

import ar.utn.ba.ddsi.fuenteProxy.repositories.IRepositoryMetamapa;
import entities.Metamapa;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FuenteProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuenteProxyApplication.class, args);
    }

    // Este método se ejecuta automáticamente al iniciar la aplicación
    @Bean
    public CommandLineRunner preloadMetamapas(IRepositoryMetamapa repository) {
        return args -> {
            Metamapa m1 = new Metamapa();
            m1.setTitulo("Mock API 1");
            m1.setUrl("https://215bc932-75a4-4afe-8a6d-87e7753f3f94.mock.pstmn.io");

            Metamapa m2 = new Metamapa();
            m2.setTitulo("Mock API 2");
            m2.setUrl("https://3988bc6c-3a3a-4189-9b74-98cb44065df4.mock.pstmn.io");

            repository.save(m1);
            repository.save(m2);

            // Podés agregar más si querés:
            // Metamapa m2 = ...
            // repository.save(m2);
        };
    }
}
