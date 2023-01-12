package br.com.iug.controller;

import br.com.iug.entity.history.ItemHistory;
import br.com.iug.entity.request.ItemRequest;
import br.com.iug.entity.response.ItemResponse;
import br.com.iug.exception.BancoNotFoundException;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.service.ItemHistoryService;
import br.com.iug.service.ItemService;
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

    @GetMapping
    public ResponseEntity<List<ItemResponse>> findAllWithParams(@RequestParam(value = "nome", required = false) String nome,
                                                      @RequestParam(value = "banco", required = false) String banco) {
        return ResponseEntity.ok(itemService.findAllWithParams(nome, banco).stream().map(ItemResponse::from).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> findById(@PathVariable("id") long id) throws ItemNotFoundException {
        return ResponseEntity.ok(ItemResponse.from(itemService.findById(id)));
    }

    @GetMapping("/gastos")
    public ResponseEntity<Double> getTotalValue(@RequestParam(value = "banco", required = false) String banco) throws BancoNotFoundException {
        return ResponseEntity.ok(itemService.getTotalValue(banco));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ItemRequest itemRequest) {
        var itemCreated = itemService.save(itemRequest.toItem());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(itemCreated.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") long id, @Valid @RequestBody ItemRequest itemRequest) throws ItemNotFoundException {
        itemService.update(id, itemRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/pay")
    public ResponseEntity<Void> payItemByBanco(@RequestParam(value = "banco") String banco) throws ItemNotFoundException {
        itemService.payByBanco(banco);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{name}/pay")
    public ResponseEntity<Void> payItem(@PathVariable("name") String nome) throws ItemNotFoundException {
        itemService.pay(nome);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws ItemNotFoundException {
        itemService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<ItemHistory>> findAllHistory(@RequestParam(value = "banco", required = false) String banco) {
        return ResponseEntity.ok(itemHistoryService.findAll(banco));
    }

    @GetMapping("/{name}/history")
    public ResponseEntity<ItemHistory> findHistoryByName(@PathVariable(value = "name") String nome) throws ItemNotFoundException {
        return ResponseEntity.ok(itemHistoryService.findByName(nome));
    }

}
