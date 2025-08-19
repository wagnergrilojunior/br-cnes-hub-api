package br.com.cneshub.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cneshub.core.dto.CkanEnvelope;
import br.com.cneshub.core.dto.EstabelecimentoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CkanClient {

    private final WebClient webClient;
    private final CkanProperties properties;
    private final ObjectMapper mapper = new ObjectMapper();

    public CkanEnvelope<EstabelecimentoDTO> buscar(String uf, String municipio, String tipo, int page, int size, String q) {
        int offset = page * size;
        Map<String, String> filtros = new HashMap<>();
        if (uf != null) filtros.put("uf", uf);
        if (municipio != null) filtros.put("municipio", municipio);
        if (tipo != null) filtros.put("co_tipo_estabelecimento", tipo);

        String filtrosJson = null;
        if (!filtros.isEmpty()) {
            try {
                filtrosJson = mapper.writeValueAsString(filtros);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Erro ao serializar filtros", e);
            }
        }

        return webClient.get()
                .uri(builder -> {
                    builder.path("/datastore_search")
                        .queryParam("resource_id", properties.getDatasets().getEstabelecimentos())
                        .queryParam("limit", size)
                        .queryParam("offset", offset);
                    if (q != null && !q.isEmpty()) {
                        builder.queryParam("q", q);
                    }
                    if (filtrosJson != null) {
                        builder.queryParam("filters", filtrosJson);
                    }
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CkanEnvelope<EstabelecimentoDTO>>() {})
                .block();
    }

    public Mono<List<String>> packageList() {
        return webClient.get()
                .uri("/package_list")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CkanPackageListEnvelope>() {})
                .map(CkanPackageListEnvelope::result)
                .doOnError(e -> log.error("Erro ao buscar lista de pacotes", e));
    }

    private record CkanPackageListEnvelope(boolean success, List<String> result) {
    }
}
