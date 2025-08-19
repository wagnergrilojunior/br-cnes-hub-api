package br.com.cneshub.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.cneshub.client.CkanProperties;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Configuration
public class WebClientConfig {

    private final CkanProperties properties;

    public WebClientConfig(CkanProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        TcpClient tcpClient = TcpClient.create()
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new io.netty.handler.timeout.ReadTimeoutHandler(10))
                        .addHandlerLast(new io.netty.handler.timeout.WriteTimeoutHandler(10)));

        HttpClient httpClient = HttpClient.from(tcpClient)
                .compress(true)
                .responseTimeout(Duration.ofSeconds(10));

        return builder
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.USER_AGENT, "cneshub-gateway")
                .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip")
                .build();
    }
}
