package br.com.cneshub.core.dto;

import java.util.Map;

import lombok.Data;

@Data
public class EstabelecimentoDTO {
    private String cnes;
    private String nome;
    private String uf;
    private String municipio;
    private String codMunicipio;
    private Map<String, Object> extra;
}
