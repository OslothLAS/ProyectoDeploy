package com.usuarios.servicioDeUsuarios.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    // Cache local para guardar los buckets por IP o Usuario (en prod usarías Redis)
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String apiKeyOrIp) {
        return cache.computeIfAbsent(apiKeyOrIp, this::createNewBucket);
    }

    private Bucket createNewBucket(String key) {
        // 3 intentos. Si se agotan, BLOQUEO TOTAL durante 1 minuto.
        // Recarga los 3 tokens DE GOLPE al pasar el minuto.
        Bandwidth limit = Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(1)));

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }


}