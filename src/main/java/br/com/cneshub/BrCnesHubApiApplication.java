package br.com.cneshub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BrCnesHubApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrCnesHubApiApplication.class, args);
    }
}
