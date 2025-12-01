package ar.utn.ba.ddsi.fuenteDinamica.models.entities.multimedia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service("cloudStorage")
public class CloudStorageStrategy implements IAlmacenamientoStrategy {

    // 🔑 Inyección del cliente S3. Debe ser configurado en una clase @Configuration.
    private final S3Client s3Client;
    
    @Value("${aws.s3.bucket-name}") 
    private String bucketName;
    
    // Prefijo de carpeta dentro del bucket
    private final String PATH_PREFIX = "hechos-multimedia/"; 

    // Constructor para inyectar el cliente S3
    public CloudStorageStrategy(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String guardarArchivo(MultipartFile archivo) throws IOException {
        if (archivo.isEmpty()) {
            throw new IOException("El archivo a subir no puede estar vacío.");
        }
        
        // 1. Crear una clave única para evitar colisiones
        String originalFileName = archivo.getOriginalFilename();
        String uniqueKey = PATH_PREFIX + UUID.randomUUID().toString() 
                           + "_" + originalFileName.replaceAll("\\s+", "_");

        try {
            // 2. Definir la solicitud (Request) para S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueKey)
                .contentType(archivo.getContentType())
                // Permite acceso público de lectura (necesario para mostrar la imagen)
                .acl("public-read") 
                .build();

            // 3. Subir el archivo al bucket
            s3Client.putObject(putObjectRequest, 
                               RequestBody.fromBytes(archivo.getBytes()));

            System.out.println(">>> [CloudStorage] Archivo subido con éxito a S3: " + uniqueKey);

            // 4. Devolver la URL pública del archivo
            return String.format("https://%s.s3.%s.amazonaws.com/%s", 
                                 bucketName, 
                                 s3Client.serviceClientConfiguration().region().id(),
                                 uniqueKey);

        } catch (S3Exception e) {
            System.err.println("Error al subir a S3: " + e.getMessage());
            throw new IOException("Fallo en la conexión o subida a AWS S3.", e);
        }
    }
}
