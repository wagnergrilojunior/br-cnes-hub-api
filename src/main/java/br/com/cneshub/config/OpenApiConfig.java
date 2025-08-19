package br.com.cneshub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("CNES Hub API")
                        .description("Gateway stateless para dados do CNES via OpenDataSUS/CKAN")
                        .version("v1"));
    }
}
