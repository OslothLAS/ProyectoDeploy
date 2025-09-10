package ar.utn.frba.ddsi.models.entities;

import org.springframework.web.reactive.function.client.WebClient;

public class AgregadorConnector {
    private final String ip;
    private final String puerto;
    private final WebClient webClient;

    public AgregadorConnector(String puerto, String ip) {
        this.puerto = puerto;
        this.ip = ip;
        this.webClient = WebClient.builder().baseUrl("http://" + ip + ":" + puerto).build();
    }


}
