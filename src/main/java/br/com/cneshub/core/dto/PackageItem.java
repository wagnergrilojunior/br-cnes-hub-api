package br.com.cneshub.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item de pacote")
public class PackageItem {

    @Schema(description = "Nome do pacote")
    private String name;
}

