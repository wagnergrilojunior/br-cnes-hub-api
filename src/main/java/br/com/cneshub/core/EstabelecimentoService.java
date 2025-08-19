package br.com.cneshub.core;

import org.springframework.stereotype.Service;

import br.com.cneshub.client.CkanClient;
import br.com.cneshub.core.dto.CkanEnvelope;
import br.com.cneshub.core.dto.CkanResponse;
import br.com.cneshub.core.dto.EstabelecimentoDTO;
import br.com.cneshub.core.dto.PageResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstabelecimentoService {

    private final CkanClient client;

    @CircuitBreaker(name = "ckan")
    @Retry(name = "ckan")
    @RateLimiter(name = "ckan")
    public CkanEnvelope<EstabelecimentoDTO> buscar(String uf, String municipio, String tipo, int page, int size, String q) {
        return client.buscar(uf, municipio, tipo, page, size, q);
    }

    public PageResponse<EstabelecimentoDTO> toPage(CkanEnvelope<EstabelecimentoDTO> envelope, int page, int size) {
        CkanResponse<EstabelecimentoDTO> result = envelope.getResult();
        return new PageResponse<>(result.getRecords(), page, size, result.getTotal());
    }
}
