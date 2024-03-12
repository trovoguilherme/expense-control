package br.com.iug.controller;

import br.com.iug.dto.request.FaturaCartaoRequest;
import br.com.iug.dto.request.FaturaCartaoUpdate;
import br.com.iug.dto.response.FaturaCartaoResponse;
import br.com.iug.entity.FaturaCartao;
import br.com.iug.exception.NotFoundException;
import br.com.iug.service.FaturaCartaoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fatura")
public class FaturaCartaoController {

    private final FaturaCartaoService faturaCartaoService;

    @Operation(summary = "Cria uma faturta de cartão")
    @PostMapping
    public ResponseEntity<FaturaCartaoResponse> save(@RequestBody FaturaCartaoRequest faturaCartaoRequest) throws NotFoundException  {
        return ResponseEntity.ok(FaturaCartaoResponse.from(faturaCartaoService.save(FaturaCartao.from(faturaCartaoRequest))));
    }

    @Operation(summary = "Busca todas as faturas de cartão")
    @GetMapping
    public ResponseEntity<List<FaturaCartaoResponse>> findAll() {
        return ResponseEntity.ok(faturaCartaoService.findAll().stream().map(FaturaCartaoResponse::from).toList());
    }

    @Operation(summary = "Busca todas as faturas de cartão por nome do pagamento")
    @GetMapping("/{nomePagamento}")
    public ResponseEntity<FaturaCartaoResponse> findByName(@PathVariable("nomePagamento") String nome) {
        return ResponseEntity.ok(FaturaCartaoResponse.from(faturaCartaoService.findByName(nome)));
    }

    @Operation(summary = "Atualiza o valor da fatura do cartão pelo nome do pagamento")
    @PatchMapping("/{nomePagamento}")
    public ResponseEntity<FaturaCartaoResponse> patchValorByName(@PathVariable("nomePagamento") String nomePagamento,
                                                                 @RequestBody FaturaCartaoUpdate faturaCartaoUpdate) {
        return ResponseEntity.ok(FaturaCartaoResponse.from(faturaCartaoService.patchValor(nomePagamento, faturaCartaoUpdate)));
    }

}
