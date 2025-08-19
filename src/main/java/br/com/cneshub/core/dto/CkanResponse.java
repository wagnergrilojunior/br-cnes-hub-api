package br.com.cneshub.core.dto;

import java.util.List;

import lombok.Data;

@Data
public class CkanResponse<T> {
    private int total;
    private int limit;
    private int offset;
    private List<T> records;
}
