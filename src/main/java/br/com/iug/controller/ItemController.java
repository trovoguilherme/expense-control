package br.com.iug.controller;

import br.com.iug.entity.enums.Banco;
import br.com.iug.entity.enums.Status;
import br.com.iug.entity.history.ItemHistory;
import br.com.iug.entity.request.ItemRequest;
import br.com.iug.entity.response.ItemResponse;
import br.com.iug.exception.BancoNotFoundException;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.exception.ItemNotUpdateParcelaException;
import br.com.iug.service.ItemHistoryService;
import br.com.iug.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    private final ItemHistoryService itemHistoryService;

    @Operation(summary = "Retorna todos os itens")
    @GetMapping
    public ResponseEntity<List<ItemResponse>> findAllWithParams(@RequestParam(value = "nome", required = false) String nome,
                                                                @RequestParam(value = "banco", required = false) String banco,
                                                                @RequestParam(value = "status", required = false) Status status) {
        return ResponseEntity.ok(itemService.findAllWithParams(nome, banco, status).stream().map(ItemResponse::from).collect(Collectors.toList()));
    }

    @Operation(summary = "Retorna o item pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> findById(@PathVariable("id") long id) throws ItemNotFoundException {
        return ResponseEntity.ok(ItemResponse.from(itemService.findById(id)));
    }

    @Operation(summary = "Cria um item")
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ItemRequest itemRequest) {
        var itemCreated = itemService.save(itemRequest.toItem());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(itemCreated.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Atualiza um item")
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> update(@PathVariable("id") long id, @Valid @RequestBody ItemRequest itemRequest) throws ItemNotFoundException, ItemNotUpdateParcelaException {
        return ResponseEntity.ok(ItemResponse.from(itemService.update(id, itemRequest)));
    }

    @Operation(summary = "Retorna os gatos totais")
    @GetMapping("/gastos")
    public ResponseEntity<Double> getTotalValue(@RequestParam(value = "banco", required = false) Banco banco,
                                                @RequestParam(value = "idsItem", required = false) List<Long> idsItem) throws BancoNotFoundException {
        return ResponseEntity.ok(itemService.getTotalValue(banco, idsItem));
    }

    @Operation(summary = "Paga os itens pelo banco")
    @PatchMapping("/pay")
    public ResponseEntity<Void> payItemByBanco(@RequestParam(value = "banco") Banco banco) throws ItemNotFoundException {
        itemService.payByBanco(banco);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Paga um item pelo id")
    @PatchMapping("/{id}/pay")
    public ResponseEntity<Void> payItem(@PathVariable("id") long id) throws ItemNotFoundException {
        itemService.pay(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deleta um item pelo id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws ItemNotFoundException {
        itemService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Retorna o histórico dos itens já pagos")
    @GetMapping("/history")
    public ResponseEntity<List<ItemHistory>> findAllHistory(@RequestParam(value = "banco", required = false) String banco) {
        return ResponseEntity.ok(itemHistoryService.findAll(banco));
    }

    @Operation(summary = "Retorna o histórico do item que já foi pago pelo nome")
    @GetMapping("/{name}/history")
    public ResponseEntity<ItemHistory> findHistoryByName(@PathVariable(value = "name") String nome) throws ItemNotFoundException {
        return ResponseEntity.ok(itemHistoryService.findByName(nome));
    }

}
