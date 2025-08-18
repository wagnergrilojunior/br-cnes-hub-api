package br.com.cneshub.core.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CkanResponse<T> {
    private int total;
    private int limit;
    private int offset;
    private List<T> records;
    private Map<String, Object> raw;
}
