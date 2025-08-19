package br.com.cneshub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.cneshub.client.CkanProperties;

@SpringBootApplication
@EnableConfigurationProperties(CkanProperties.class)
public class BrCnesHubApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrCnesHubApiApplication.class, args);
    }
}
