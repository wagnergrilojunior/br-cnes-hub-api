package br.com.cneshub.core.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta da lista de pacotes")
public class PackageListResponse {

    @Schema(description = "Total de pacotes")
    private int total;

    @Schema(description = "Página atual (0-based)")
    private int page;

    @Schema(description = "Tamanho da página")
    private int size;

    @Schema(description = "Itens da página")
    private List<PackageItem> items;
}

