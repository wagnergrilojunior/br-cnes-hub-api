package br.com.cneshub.ingestor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.cneshub.core.dto.CkanResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Mono;

/**
 * Client to interact with CKAN API using WebClient.
 */
@Component
public class CkanClient {

    private final WebClient webClient;
    private final Map<String, String> etags = new ConcurrentHashMap<>();

    public CkanClient(WebClient ckanWebClient, @Value("${cneshub.ingest.baseUrl}") String baseUrl) {
        this.webClient = ckanWebClient.mutate().baseUrl(baseUrl).build();
    }

    @CircuitBreaker(name = "ckan")
    @Retry(name = "ckan")
    @RateLimiter(name = "ckan")
    public CkanResponse<Map<String, Object>> datastoreSearch(String resourceId, Map<String, String> params, int offset, int limit) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/datastore_search")
                .queryParam("resource_id", resourceId)
                .queryParam("offset", offset)
                .queryParam("limit", limit);
        if (params != null) {
            params.forEach(builder::queryParam);
        }
        String uri = builder.toUriString();
        WebClient.RequestHeadersSpec<?> request = webClient.get().uri(uri);
        String etag = etags.get(uri);
        if (etag != null) {
            request = request.header(HttpHeaders.IF_NONE_MATCH, etag);
        }
        return request.exchangeToMono(resp -> {
            if (resp.statusCode().equals(HttpStatus.NOT_MODIFIED)) {
                return Mono.just(new CkanResponse<>(0, limit, offset, List.of(), Collections.emptyMap()));
            }
            if (!resp.statusCode().is2xxSuccessful()) {
                return resp.createException().flatMap(Mono::error);
            }
            String newEtag = resp.headers().asHttpHeaders().getFirst(HttpHeaders.ETAG);
            if (newEtag != null) {
                etags.put(uri, newEtag);
            }
            return resp.bodyToMono(Map.class).map(this::toCkanResponse);
        }).block();
    }

    @CircuitBreaker(name = "ckan")
    @Retry(name = "ckan")
    @RateLimiter(name = "ckan")
    public CkanResponse<Map<String, Object>> datastoreSearchSql(String sql) {
        String uri = UriComponentsBuilder.fromPath("/datastore_search_sql")
                .queryParam("sql", sql).toUriString();
        WebClient.RequestHeadersSpec<?> request = webClient.get().uri(uri);
        String etag = etags.get(uri);
        if (etag != null) {
            request = request.header(HttpHeaders.IF_NONE_MATCH, etag);
        }
        return request.exchangeToMono(resp -> {
            if (resp.statusCode().equals(HttpStatus.NOT_MODIFIED)) {
                return Mono.just(new CkanResponse<>(0, 0, 0, List.of(), Collections.emptyMap()));
            }
            if (!resp.statusCode().is2xxSuccessful()) {
                return resp.createException().flatMap(Mono::error);
            }
            String newEtag = resp.headers().asHttpHeaders().getFirst(HttpHeaders.ETAG);
            if (newEtag != null) {
                etags.put(uri, newEtag);
            }
            return resp.bodyToMono(Map.class).map(this::toCkanResponse);
        }).block();
    }

    @SuppressWarnings("unchecked")
    private CkanResponse<Map<String, Object>> toCkanResponse(Map<String, Object> body) {
        Map<String, Object> result = (Map<String, Object>) body.getOrDefault("result", Collections.emptyMap());
        int total = ((Number) result.getOrDefault("total", 0)).intValue();
        int limit = ((Number) result.getOrDefault("limit", 0)).intValue();
        int offset = ((Number) result.getOrDefault("offset", 0)).intValue();
        List<Map<String, Object>> records = (List<Map<String, Object>>) result.getOrDefault("records", List.of());
        return new CkanResponse<>(total, limit, offset, records, result);
    }
}
