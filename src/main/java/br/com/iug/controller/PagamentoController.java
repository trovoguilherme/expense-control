package br.com.iug.controller;

import br.com.iug.entity.Pagamento;
import br.com.iug.dto.request.PagamentoRequest;
import br.com.iug.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "Salva um pagamento")
    @PostMapping
    public ResponseEntity<Pagamento> save(@Valid @RequestBody PagamentoRequest pagamentoRequest) {
        return ResponseEntity.ok(pagamentoService.save(pagamentoRequest.toPagamento()));
    }

    @Operation(summary = "Busca todos os nomes de pagamentos")
    @GetMapping
    public ResponseEntity<List<Pagamento>> findAll() {
        return ResponseEntity.ok(pagamentoService.findAll());
    }

    @Operation(summary = "Deleta um pagamento pelo nome")

    @DeleteMapping("/{nome}")
    public ResponseEntity<Void> deleteByName(@PathVariable("nome") String nome) {
        pagamentoService.deleteByName(nome);
        return ResponseEntity.ok().build();
    }

}
