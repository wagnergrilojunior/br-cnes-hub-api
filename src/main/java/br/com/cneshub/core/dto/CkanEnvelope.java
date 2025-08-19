package br.com.cneshub.core.dto;

import lombok.Data;

@Data
public class CkanEnvelope<T> {
    private boolean success;
    private CkanResponse<T> result;
}
