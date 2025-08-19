package br.com.cneshub.api.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cneshub.core.dto.PackageListResponse;
import br.com.cneshub.core.service.CnesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
@Validated
@Tag(name = "Catálogo")
public class CatalogController {

    private final CnesService service;

    @GetMapping("/packages")
    @Operation(summary = "Lista pacotes", description = "Retorna lista de datasets do CKAN")
    public ResponseEntity<PackageListResponse> listarPacotes(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(200) int size,
            @RequestParam(required = false) String q) {
        PackageListResponse response = service.listarPacotes(page, size, q);
        return ResponseEntity.ok().body(response);
    }
}

