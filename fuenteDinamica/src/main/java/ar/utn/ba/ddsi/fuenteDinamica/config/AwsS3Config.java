// En un paquete de configuración, ej: ar.utn.ba.ddsi.fuenteDinamica.config

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AwsS3Config {

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public S3Client s3Client() {
        // Inicializa el cliente S3 usando la región configurada
        return S3Client.builder()
            .region(Region.of(awsRegion))
            // El SDK buscará automáticamente las credenciales en variables de entorno,
            // archivos ~/.aws/credentials o roles de IAM si estás en EC2/ECS.
            .build();
    }
}
