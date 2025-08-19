package br.com.cneshub.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "cneshub.ingest")
public class CkanProperties {

    private String baseUrl;
    private Datasets datasets = new Datasets();

    @Data
    public static class Datasets {
        private String estabelecimentos;
    }
}
