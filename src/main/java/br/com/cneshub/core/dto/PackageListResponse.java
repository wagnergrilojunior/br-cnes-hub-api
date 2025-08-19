package br.com.cneshub.core.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Resposta da lista de pacotes")
public class PackageListResponse {

    @ApiModelProperty(value = "Total de pacotes")
    private int total;

    @ApiModelProperty(value = "Página atual (0-based)")
    private int page;

    @ApiModelProperty(value = "Tamanho da página")
    private int size;

    @ApiModelProperty(value = "Itens da página")
    private List<PackageItem> items;
}

