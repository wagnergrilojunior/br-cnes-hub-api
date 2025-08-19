package br.com.cneshub.api.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import br.com.cneshub.core.service.EstabelecimentoService;
import br.com.cneshub.core.dto.CkanEnvelope;
import br.com.cneshub.core.dto.EstabelecimentoDTO;
import br.com.cneshub.core.dto.PageResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Estabelecimentos")
public class EstabelecimentoController {

    private final EstabelecimentoService service;

    @GetMapping("/estabelecimentos")
    @Operation(summary = "Lista estabelecimentos", description = "Retorna lista paginada de estabelecimentos do CNES")
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "false") boolean raw) {

        CkanEnvelope<EstabelecimentoDTO> envelope = service.buscar(uf, municipio, tipo, page, size, q);
        if (raw) {
            return ResponseEntity.ok(envelope);
        }
        PageResponse<EstabelecimentoDTO> pagina = service.toPage(envelope, page, size);
        return ResponseEntity.ok(pagina);
    }
}
