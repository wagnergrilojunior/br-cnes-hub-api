package br.com.cneshub.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EstabelecimentoDTO {

    @JsonProperty("co_cnes")
    private String codigoCnes;

    @JsonProperty("no_fantasia")
    private String nomeFantasia;

    @JsonProperty("co_tipo_estabelecimento")
    private String tipo;

    @JsonProperty("municipio")
    private String municipio;

    @JsonProperty("uf")
    private String uf;
}
