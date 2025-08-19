package br.com.cneshub.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Item de pacote")
public class PackageItem {

    @ApiModelProperty(value = "Nome do pacote")
    private String name;
}

