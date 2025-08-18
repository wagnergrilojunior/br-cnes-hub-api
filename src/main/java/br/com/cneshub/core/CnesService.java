package br.com.cneshub.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.cneshub.core.dto.CkanResponse;
import br.com.cneshub.core.dto.EstabelecimentoDTO;
import br.com.cneshub.ingestor.CkanClient;

@Service
public class CnesService {

    private final CkanClient ckanClient;
    private final String resourceId;

    public CnesService(CkanClient ckanClient,
                       @Value("${cneshub.ingest.datasets.estabelecimentos}") String resourceId) {
        this.ckanClient = ckanClient;
        this.resourceId = resourceId;
    }

    @Cacheable(cacheNames = "estabelecimentos", key = "T(br.com.cneshub.core.CnesService).cacheKey(#filtros,#page,#size,#raw)")
    public CkanResponse<EstabelecimentoDTO> buscarEstabelecimentos(Map<String, String> filtros, int page, int size, boolean raw) {
        int offset = Math.max(page, 0) * size;
        CkanResponse<Map<String, Object>> response = ckanClient.datastoreSearch(resourceId, filtros, offset, size);
        List<EstabelecimentoDTO> records = response.getRecords().stream()
                .map(map -> toDto(map, raw))
                .collect(Collectors.toList());
        return new CkanResponse<>(response.getTotal(), response.getLimit(), response.getOffset(), records,
                raw ? response.getRaw() : null);
    }

    private EstabelecimentoDTO toDto(Map<String, Object> record, boolean raw) {
        EstabelecimentoDTO dto = new EstabelecimentoDTO();
        dto.setCnes((String) record.get("cnes"));
        dto.setNome((String) record.get("nome"));
        dto.setUf((String) record.get("uf"));
        dto.setMunicipio((String) record.get("municipio"));
        dto.setCodMunicipio((String) record.get("cod_municipio"));
        if (raw) {
            dto.setExtra(record);
        }
        return dto;
    }

    public static String cacheKey(Map<String, String> filtros, int page, int size, boolean raw) {
        String filtrosKey = filtros == null ? "" : filtros.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + '=' + e.getValue())
                .collect(Collectors.joining("&"));
        return filtrosKey + '|' + page + '|' + size + '|' + raw;
    }
}
