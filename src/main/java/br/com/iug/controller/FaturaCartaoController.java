package br.com.iug.controller;

import br.com.iug.dto.request.FaturaCartaoRequest;
import br.com.iug.dto.request.FaturaCartaoUpdate;
import br.com.iug.dto.response.FaturaCartaoResponse;
import br.com.iug.entity.FaturaCartao;
import br.com.iug.exception.NotFoundException;
import br.com.iug.service.FaturaCartaoService;
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

    @PostMapping
    public ResponseEntity<FaturaCartaoResponse> save(@RequestBody FaturaCartaoRequest faturaCartaoRequest) throws NotFoundException  {
        return ResponseEntity.ok(FaturaCartaoResponse.from(faturaCartaoService.save(FaturaCartao.from(faturaCartaoRequest))));
    }

    @GetMapping
    public ResponseEntity<List<FaturaCartaoResponse>> findAll() {
        return ResponseEntity.ok(faturaCartaoService.findAll().stream().map(FaturaCartaoResponse::from).toList());
    }

    @GetMapping("/{nome}")
    public ResponseEntity<FaturaCartaoResponse> findByName(@PathVariable("nome") String nome) {
        return ResponseEntity.ok(FaturaCartaoResponse.from(faturaCartaoService.findByName(nome)));
    }

    @PatchMapping("/{nomePagamento}")
    public ResponseEntity<FaturaCartaoResponse> patchValorByName(@PathVariable("nomePagamento") String nomePagamento,
                                                                 @RequestBody FaturaCartaoUpdate faturaCartaoUpdate) {
        return ResponseEntity.ok(FaturaCartaoResponse.from(faturaCartaoService.patchValor(nomePagamento, faturaCartaoUpdate)));
    }

}
