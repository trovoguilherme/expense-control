package br.com.iug.controller;

import br.com.iug.entity.Pagamento;
import br.com.iug.dto.request.PagamentoRequest;
import br.com.iug.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pagamento")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<Pagamento> save(@Valid @RequestBody PagamentoRequest pagamentoRequest) {
        return ResponseEntity.ok(pagamentoService.save(pagamentoRequest.toPagamento()));
    }

    @GetMapping
    public ResponseEntity<List<Pagamento>> findAll() {
        return ResponseEntity.ok(pagamentoService.findAll());
    }

}
